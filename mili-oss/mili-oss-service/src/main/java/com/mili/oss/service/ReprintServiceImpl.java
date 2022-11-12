package com.mili.oss.service;

import com.alibaba.fastjson.JSON;
import com.farmland.core.util.BeanMapper;
import com.mili.oss.api.ReprintService;
import com.mili.oss.constant.OrderTypeEnum;
import com.mili.oss.constant.PrintTypeEnum;
import com.mili.oss.domain.OrderSet;
import com.mili.oss.domain.OrderSetExample;
import com.mili.oss.domain.OrderSetGoods;
import com.mili.oss.domain.OrderSetGoodsExample;
import com.mili.oss.dto.NotifyOrderSetQueryResult;
import com.mili.oss.dto.OrderSetDetailDto;
import com.mili.oss.dto.OrderSetDetailGoodsDto;
import com.mili.oss.dto.OrderSetProductionMsgByPrint;
import com.mili.oss.mapper.OrderSetGoodsMapper;
import com.mili.oss.mapper.OrderSetMapper;
import com.mili.oss.mq.message.*;
import com.mili.oss.properties.ShopIdProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @program: mili-oss
 * @description: 票据补打接口实现
 * @author: liuyy
 * @date: 2019/5/28 19:49
 */
@Slf4j
@Service("ReprintService")
@RestController
public class ReprintServiceImpl implements ReprintService {

    @Autowired
    private OrderSetMapper orderSetMapper;

    @Autowired
    private OrderSetGoodsMapper orderSetGoodsMapper;

    @Autowired OrderServiceImpl orderServiceImpl;

    @Autowired
    private ShopIdProperties shopIdProperties;
    /***
     * BOSS端午餐补打
     *
     */
    @Override
    public NotifyOrderSetQueryResult reprintSetMqBySetNo(@RequestParam("setNo") String setNo,@RequestParam("coupletNo") String coupletNo,@RequestParam("printType") Integer printType) {
        log.info("scm午餐传递集单号setNo{}",setNo);
        log.info("scm午餐传递打印类型{}",printType);
        log.info("scm午餐传递顾客联号{}",coupletNo);
        NotifyOrderSetQueryResult result = new NotifyOrderSetQueryResult();
        OrderSetExample example = new OrderSetExample();
        example.createCriteria().andDetailSetNoEqualTo(setNo);
        List<OrderSet> orderSetList = orderSetMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(orderSetList)) {
            return null;
        }
        result.setOrderSet(BeanMapper.map(orderSetList.get(0), OrderSetDetailDto.class));
        OrderSetGoodsExample exampleGoods = new OrderSetGoodsExample();
        exampleGoods.createCriteria().andDetailSetNoEqualTo(setNo);
        List<OrderSetGoods> listGoods = orderSetGoodsMapper.selectByExample(exampleGoods);
        if (CollectionUtils.isEmpty(listGoods)) {
            return null;
        }
        result.setGoodsList(BeanMapper.mapList(listGoods, OrderSetDetailGoodsDto.class));

        log.info("1号店门店ID{}",shopIdProperties.getShopIdOne());
        log.info("2号店门店ID{}",shopIdProperties.getShopIdTwo());
        log.info("集单门店ID{}",orderSetList.get(0).getShopId());
        if(shopIdProperties.getShopIdOne().equals(orderSetList.get(0).getShopId())
            || shopIdProperties.getShopIdTwo().equals(orderSetList.get(0).getShopId())){

        }else {
            //--------------------boss端补打发送MQ消息---------------------------------
                processRePrintOrderSetMqLunch(orderSetList, orderSetList.get(0).getShopId(),printType,coupletNo);
        }
        return result;
    }

    /***
     * BOSS端早餐补打
     *
     */
    @Override
    public OrderSetProductionMsgByPrint reprintBfSetMqBySetNo(String setNo, String coupletNo, Integer printType) {
        log.info("scm早餐传递集单号setNo{}",setNo);
        log.info("scm早餐传递打印类型{}",printType);
        log.info("scm早餐传递顾客联号{}",coupletNo);

        OrderSetProductionMsgByPrint result = new OrderSetProductionMsgByPrint();
        OrderSetExample example = new OrderSetExample();
        example.createCriteria().andDetailSetNoEqualTo(setNo);
        List<OrderSet> orderSetList = orderSetMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(orderSetList)) {
            return null;
        }
        result.setOrderSet(BeanMapper.map(orderSetList.get(0), OrderSetDetailDto.class));
        OrderSetGoodsExample exampleGoods = new OrderSetGoodsExample();
        exampleGoods.createCriteria().andDetailSetNoEqualTo(setNo);
        List<OrderSetGoods> listGoods = orderSetGoodsMapper.selectByExample(exampleGoods);
        if (CollectionUtils.isEmpty(listGoods)) {
            return null;
        }
        result.setGoods(BeanMapper.mapList(listGoods, OrderSetDetailGoodsDto.class));

        log.info("1号店门店ID{}",shopIdProperties.getShopIdOne());
        log.info("2号店门店ID{}",shopIdProperties.getShopIdTwo());
        log.info("集单门店ID{}",orderSetList.get(0).getShopId());
        if(shopIdProperties.getShopIdOne().equals(orderSetList.get(0).getShopId())
                || shopIdProperties.getShopIdTwo().equals(orderSetList.get(0).getShopId())){

        }else {
            //--------------------boss端补打发送MQ消息---------------------------------
                processRePrintOrderSetMq(orderSetList, orderSetList.get(0).getShopId(),printType,coupletNo);
        }
        return result;
    }

    /***
     * 午餐补打逻辑
     *
     */
    private void processRePrintOrderSetMqLunch(List<OrderSet> orderSetList, Long shopId, Integer printType, String coupletNo) {
        List<WaveOrderMessage> waveOrderMessageList = new ArrayList<>(100);
        WaveOrderMessage currentWaveOrderMessage = new WaveOrderMessage();

        OrderSet item = orderSetList.get(0);

        OrderSetGoodsExample example = new OrderSetGoodsExample();
        example.createCriteria().andDetailSetNoEqualTo(item.getDetailSetNo());
        List<OrderSetGoods> details = orderSetGoodsMapper.selectByExample(example);
        //打包信息组装
        DeliveryPackageMessage deliveryPackageMessage = rePrintBuildMessageLunch(item, details,printType,coupletNo);

        currentWaveOrderMessage.setCompany(item.getCompanyAbbreviation());
        currentWaveOrderMessage.setCompanyAddr(item.getDetailDeliveryAddress());
        currentWaveOrderMessage.setShopId(shopId.toString());
        currentWaveOrderMessage.setUserPackageCount(deliveryPackageMessage.getUserPackageCount());
        currentWaveOrderMessage.setBusinessLine(item.getOrderType());
        currentWaveOrderMessage.addDeliveryPackage(deliveryPackageMessage);
        currentWaveOrderMessage.setPrintType(printType);
        waveOrderMessageList.add(currentWaveOrderMessage);

        WaveOrderDispatcher.dispatch(waveOrderMessageList, orderServiceImpl.getPrinterCounter(shopId));

    }


    /***
     * 午餐补打逻辑
     *
     */
    public DeliveryPackageMessage rePrintBuildMessageLunch(OrderSet item, List<OrderSetGoods> details, Integer printType, String coupletNo) {
        //按顾客联编号分组
        TreeMap<String, List<OrderSetGoods>> goodsByReceiptNo = details.stream().collect(groupingBy(OrderSetGoods::getCoupletNo,TreeMap::new, Collectors.toList()));
        List<UserPackageMessage> userPackageMessageList = new ArrayList<>(1000);

        //只打印顾客联时，删除其他顾客联信息
        if(PrintTypeEnum.CONSUMEPRINT.getValue().equals(printType)){
            Iterator iterator = goodsByReceiptNo.keySet().iterator();
            while (iterator.hasNext()){
                String removeKey = (String)iterator.next();
                if(!removeKey.equals(coupletNo)){
                    iterator.remove();
                    goodsByReceiptNo.remove(removeKey);
                }
            }
        }

        Map<String,List<OrderSetGoods>> picklesMap = new HashMap<String,List<OrderSetGoods>>();
        Map<String,List<PrintLunchDto>> printLunchMap = new TreeMap<String,List<PrintLunchDto>>();

        int copiesNumber = 0 ;

        for (Map.Entry<String, List<OrderSetGoods>> entry : goodsByReceiptNo.entrySet()) {
            //打包的商品
            List<PrintLunchDto> printLunch = new ArrayList<PrintLunchDto>();
            List<OrderSetGoods> listCustomer = entry.getValue();
            for(OrderSetGoods orderSetGoodsDto : listCustomer){
                //非主餐的商品根据订单号单独存放
                if(orderSetGoodsDto.getType() != (byte)0) {
                    List<OrderSetGoods> listpick =	picklesMap.get(entry.getKey());
                    if(CollectionUtils.isEmpty(listpick)) {
                        listpick = Lists.newArrayList();
                    }
                    listpick.add(orderSetGoodsDto);
                    picklesMap.put(entry.getKey(), listpick);
                }else {
                    //主餐
                    Integer goodsCount = orderSetGoodsDto.getGoodsNumber();
                    for (int i = 0; i < goodsCount; i++) {
                        List<OrderSetGoods> customerInfo = new ArrayList<OrderSetGoods>();
                        orderSetGoodsDto.setGoodsNumber(1);
                        customerInfo.add(orderSetGoodsDto);
                        PrintLunchDto p = new PrintLunchDto();
                        p.setCoupletNo(entry.getKey());
                        p.setCustomerInfo(customerInfo);
                        //p.setOrderSetPrintMap(orderSetPrintMap);
                        copiesNumber++;
                        printLunch.add(p);
                    }
                }
            }
            printLunchMap.put(entry.getKey(), printLunch);
        }
        int i = 0;
        //将小菜 存放在任意顾客联中
        for (String userId : printLunchMap.keySet()) {
            List<PrintLunchDto> listlunch = printLunchMap.get(userId);
            List<OrderSetGoods> listUserIdPick = picklesMap.get(userId);
            if(!CollectionUtils.isEmpty(listUserIdPick)) {
                //顾客没有点主餐
                if(CollectionUtils.isEmpty(listlunch)) {
                    listlunch = Lists.newArrayList();
                    PrintLunchDto p = new PrintLunchDto();
                    p.setCoupletNo(userId);
                    p.setCustomerInfo(listUserIdPick);
                    //p.setOrderSetPrintMap(orderSetPrintMap);
                    copiesNumber++;
                    listlunch.add(p);
                    printLunchMap.put(userId, listlunch);
                }else {
                    //顾客点了主餐
                    listlunch.get(0).getCustomerInfo().addAll(listUserIdPick);
                }
            }
        }
        //开始组装顾客联信息
        for (List<PrintLunchDto> orderDetailInfo : printLunchMap.values()) {
            for (PrintLunchDto printLunchDto : orderDetailInfo) {
                i ++; //张数计算

                UserPackageMessage userPackageMessage = new UserPackageMessage();
                userPackageMessage.setMobileNo(printLunchDto.getCustomerInfo().get(0).getUserPhone());
                userPackageMessage.setReceiptNo(printLunchDto.getCoupletNo());
                userPackageMessage.setUserName(printLunchDto.getCustomerInfo().get(0).getUserName());
                userPackageMessage.setIndex(i);
                List<UserPackageGoodsMessage> itemDetailList = new ArrayList<>();
                for(OrderSetGoods eachItem:printLunchDto.getCustomerInfo()){
                    UserPackageGoodsMessage itemmesg = new UserPackageGoodsMessage();
                    itemmesg.setGoodsName(eachItem.getGoodsName());
                    itemmesg.setGoodsCount(eachItem.getGoodsNumber());
                    itemDetailList.add(itemmesg);
                }
                userPackageMessage.setItemGoodsList(itemDetailList);
                userPackageMessageList.add(userPackageMessage);
            }
        }
        DeliveryPackageMessage deliveryPackageMessage = new DeliveryPackageMessage();
        deliveryPackageMessage.setDeliveryNo("#" + Integer.parseInt(item.getDetailSetNoDescription().substring(1)));
        deliveryPackageMessage.setDeliveryDate(DateFormatUtils.format(item.getDetailDeliveryDate(), "yyyy-MM-dd"));
        deliveryPackageMessage.setTakeMealAddr(item.getMealAddress());
        deliveryPackageMessage.setTakeMealStartTime(DateFormatUtils.format(item.getDeliveryStartTime(), "HH:mm"));
        deliveryPackageMessage.setTakeMealEndTime(DateFormatUtils.format(item.getDeliveryEndTime(), "HH:mm"));
        deliveryPackageMessage.setUserPackageList(userPackageMessageList);
        deliveryPackageMessage.setUserPackageCount(userPackageMessageList.size());
        return deliveryPackageMessage;
    }

    /***
     * 早餐补打逻辑
     *
     */
    private void processRePrintOrderSetMq(List<OrderSet> orderSetList, Long shopId, Integer printType, String coupletNo) {
        List<WaveOrderMessage> waveOrderMessageList = new ArrayList<>(100);
        WaveOrderMessage currentWaveOrderMessage = new WaveOrderMessage();

        OrderSet item = orderSetList.get(0);

        OrderSetGoodsExample example = new OrderSetGoodsExample();
        example.createCriteria().andDetailSetNoEqualTo(item.getDetailSetNo());
        List<OrderSetGoods> details = orderSetGoodsMapper.selectByExample(example);
        //打包信息组装
        DeliveryPackageMessage deliveryPackageMessage = rePrintBuildMessage(item, details,printType,coupletNo);

        currentWaveOrderMessage.setCompany(item.getCompanyAbbreviation());
        currentWaveOrderMessage.setCompanyAddr(item.getDetailDeliveryAddress());
        currentWaveOrderMessage.setShopId(shopId.toString());
        currentWaveOrderMessage.setUserPackageCount(deliveryPackageMessage.getUserPackageCount());
        currentWaveOrderMessage.setBusinessLine(item.getOrderType());
        currentWaveOrderMessage.addDeliveryPackage(deliveryPackageMessage);
        currentWaveOrderMessage.setPrintType(printType);
        waveOrderMessageList.add(currentWaveOrderMessage);
        WaveOrderDispatcher.dispatch(waveOrderMessageList, orderServiceImpl.getPrinterCounter(shopId));
    }

    /***
     * 早餐打包详细信息组装
     *
     */
    public DeliveryPackageMessage rePrintBuildMessage(OrderSet item, List<OrderSetGoods> details, Integer printType, String coupletNo) {
        //按顾客联编号分组
        TreeMap<String, List<OrderSetGoods>> goodsByReceiptNo = details.stream().collect(groupingBy(OrderSetGoods::getCoupletNo,TreeMap::new, Collectors.toList()));
        List<UserPackageMessage> userPackageMessageList = new ArrayList<>(1000);

        //只打印顾客联时，删除其他顾客联信息
        if(PrintTypeEnum.CONSUMEPRINT.getValue().equals(printType)){
            Iterator iterator = goodsByReceiptNo.keySet().iterator();
            while (iterator.hasNext()){
                String removeKey = (String)iterator.next();
                if(!removeKey.equals(coupletNo)){
                    iterator.remove();
                    goodsByReceiptNo.remove(removeKey);
                }
            }
        }

        int number = 0;
        int index=0;
        for (Map.Entry<String, List<OrderSetGoods>> entry : goodsByReceiptNo.entrySet()) {
            //得到顾客联编号
            String receiptNo = entry.getKey();
            //打包的商品
            List<OrderSetGoods> listCustomer = entry.getValue();
            OrderSetGoods first = listCustomer.get(0);

            Map<String, Object> mapResult = getCustomerDateComb(listCustomer);
            Object itemObj = mapResult.get("item");
            Object comboObj = mapResult.get("combo");
            log.info("处理单品和 套餐的数据{}", JSON.toJSONString(mapResult));

            int itemCount = 0;
            int comboCount = 0;

            List<OrderSetGoods> itemGoodsList =	(List<OrderSetGoods>)itemObj;
            Map<String,List<OrderSetGoods>> comboMap = (Map<String, List<OrderSetGoods>>) comboObj;

            //Map<Boolean, List<OrderSetGoods>> itemOrCombo = goodsList.stream().collect(groupingBy(OrderSetGoods::getIsCombo));
            //单品和套餐都有的情况下
            PrintAllBreakFastDto allDate = new PrintAllBreakFastDto();
            allDate.setCoupletNo(entry.getKey());
            if(!CollectionUtils.isEmpty(itemGoodsList) && ! CollectionUtils.isEmpty(comboMap) ) {
                number += goodsPrintForBreakFast(receiptNo, mapResult,allDate);
            }else {
                List<PrintBreakFastDto>	singleCountDate = itemGoodsPrint(receiptNo, itemGoodsList);
                itemCount = singleCountDate.size();
                List<PrintBreakFastDto>	comboCountDate = comboGoodsPrint(receiptNo, comboMap);
                comboCount = comboCountDate.size();

                allDate.setComboCountDate(comboCountDate);
                allDate.setSingleCountDate(singleCountDate);

            }
            //listAllDate.add(allDate);
            number += itemCount + comboCount;
            log.info("顾客:{},一共{}份",entry.getKey(),number);
            //开始组装顾客联

            //单品处理
            List<PrintBreakFastDto> items = allDate.getSingleCountDate();
            List<UserPackageGoodsMessage> itemPackGoodsMessageList = null;
            for(int i=0;i<items.size();i++){
                index++;
                UserPackageMessage userPackageMessage = new UserPackageMessage();
                userPackageMessage.setMobileNo(first.getUserPhone());
                userPackageMessage.setReceiptNo(receiptNo);
                userPackageMessage.setUserName(first.getUserName());
                userPackageMessage.setIndex(index);
                List<UserPackageGoodsMessage> eachConsumer = new ArrayList<>();
                for (OrderSetGoods goodsDetail:items.get(i).getListOrderSetDetail()) {
                    UserPackageGoodsMessage userPackageGoodsMessage = new UserPackageGoodsMessage();
                    userPackageGoodsMessage.setGoodsName(goodsDetail.getGoodsName());
                    userPackageGoodsMessage.setGoodsCount(goodsDetail.getGoodsNumber());
                    eachConsumer.add(userPackageGoodsMessage);
                }
                userPackageMessage.setItemGoodsList(eachConsumer);
                userPackageMessageList.add(userPackageMessage);
            }

            //套餐处理
            List<PrintBreakFastDto> combos = allDate.getComboCountDate();
            List<UserPackageGoodsMessage> comboPackGoodsMessageList = null;
            for(int i = 0;i<combos.size();i++){
                index++;
                UserPackageMessage userPackageMessage = new UserPackageMessage();
                userPackageMessage.setMobileNo(first.getUserPhone());
                userPackageMessage.setReceiptNo(receiptNo);
                userPackageMessage.setUserName(first.getUserName());
                userPackageMessage.setIndex(index);
                //组装每个顾客联套餐详细信息
                //套餐对象
                List<UserPackageGoodsMessage> eachComboConsumer = new ArrayList<>();
                List<OrderSetGoods> combogoodsDetail = combos.get(i).getListOrderSetDetail();
                UserPackageGoodsMessage combosmesg = new UserPackageGoodsMessage();
                combosmesg.setGoodsName(combogoodsDetail.get(0).getComboGoodsName());
                combosmesg.setGoodsCount(combogoodsDetail.get(0).getComboGoodsCount());
                List<UserPackageGoodsMessage> comboItemDetailList = new ArrayList<>();
                for (OrderSetGoods itemgoodsDetail : combogoodsDetail){
                    UserPackageGoodsMessage itemmesg = new UserPackageGoodsMessage();
                    itemmesg.setGoodsName(itemgoodsDetail.getGoodsName());
                    itemmesg.setGoodsCount(itemgoodsDetail.getGoodsNumber()/itemgoodsDetail.getComboGoodsCount());
                    comboItemDetailList.add(itemmesg);
                }
                combosmesg.setItems(comboItemDetailList);
                eachComboConsumer.add(combosmesg);
                userPackageMessage.setComboGoodsList(eachComboConsumer);
                //如果这个套餐中还有单品
                List<OrderSetGoods> itemgoodsDetail = combos.get(i).getSurplusSingle();
                if (itemgoodsDetail != null){
                    List<UserPackageGoodsMessage> itemDetailsList = new ArrayList<>();
                    for(OrderSetGoods itemgoos : itemgoodsDetail){
                        UserPackageGoodsMessage itemmesg = new UserPackageGoodsMessage();
                        itemmesg.setGoodsName(itemgoos.getGoodsName());
                        itemmesg.setGoodsCount(itemgoos.getGoodsNumber());
                        itemDetailsList.add(itemmesg);
                    }
                    userPackageMessage.setItemGoodsList(itemDetailsList);
                }
                userPackageMessageList.add(userPackageMessage);
            }
        }
        DeliveryPackageMessage deliveryPackageMessage = new DeliveryPackageMessage();
        deliveryPackageMessage.setDeliveryNo("#" + Integer.parseInt(item.getDetailSetNoDescription().substring(1)));
        deliveryPackageMessage.setDeliveryDate(DateFormatUtils.format(item.getDetailDeliveryDate(), "yyyy-MM-dd"));
        deliveryPackageMessage.setTakeMealAddr(item.getMealAddress());
        deliveryPackageMessage.setTakeMealStartTime(DateFormatUtils.format(item.getDeliveryStartTime(), "HH:mm"));
        deliveryPackageMessage.setTakeMealEndTime(DateFormatUtils.format(item.getDeliveryEndTime(), "HH:mm"));
        deliveryPackageMessage.setUserPackageList(userPackageMessageList);
        deliveryPackageMessage.setUserPackageCount(userPackageMessageList.size());
        return deliveryPackageMessage;
    }



    private Map<String, Object> getCustomerDateComb(List<OrderSetGoods> orderSetDetailGoodsDto) {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        Map<String,List<OrderSetGoods>> comboMap = new HashMap<String,List<OrderSetGoods>>();
        List<OrderSetGoods> itemList = new ArrayList<OrderSetGoods>();
        Integer comboCount = 0; //组合品中单品总数量
        Integer itemCount = 0;
        Integer comboSumCount = 0;   //套餐总数量
        for (OrderSetGoods eachOrderSetGoodsDto : orderSetDetailGoodsDto) {
            if(org.apache.commons.lang3.BooleanUtils.isNotFalse(eachOrderSetGoodsDto.getIsCombo())) {
                String comboGoodsCode = eachOrderSetGoodsDto.getComboGoodsCode();
                List<OrderSetGoods> list = comboMap.get(comboGoodsCode);
                if(CollectionUtils.isEmpty(list)) {
                    list = new ArrayList<OrderSetGoods>();
                    comboSumCount += eachOrderSetGoodsDto.getComboGoodsCount();
                }
                comboCount +=eachOrderSetGoodsDto.getGoodsNumber();
                list.add(eachOrderSetGoodsDto);
                comboMap.put(comboGoodsCode, list);
            }else {
                if(eachOrderSetGoodsDto.getGoodsNumber() >= 1) {
                    for (int i = 1; i <= eachOrderSetGoodsDto.getGoodsNumber(); i++) {
                        OrderSetGoods orderSetDetailGppds = BeanMapper.map(eachOrderSetGoodsDto, OrderSetGoods.class);
                        orderSetDetailGppds.setGoodsNumber(1);
                        itemList.add(orderSetDetailGppds);
                    }
                }else {
                    itemList.add(eachOrderSetGoodsDto);
                }
                itemCount += eachOrderSetGoodsDto.getGoodsNumber();
            }
        }
        resultMap.put("combo", comboMap);//单个集单中套餐map
        resultMap.put("comboCount", comboCount);//单个集单中套餐下商品总数
        resultMap.put("comboSumCount", comboSumCount);//单个集单中套餐总数量
        resultMap.put("item", itemList);//单个集单中所有单品一个个拆开后的list
        resultMap.put("singleCount", itemCount);//单个集单中单品的总数
        return resultMap;
    }

    private int goodsPrintForBreakFast(String userId, Map<String, Object> mapResult, PrintAllBreakFastDto allDate) {
        Object singleObj = mapResult.get("item");
        Object comboObj = mapResult.get("combo");
        Integer comboCount = (Integer)mapResult.get("comboCount");
        Integer comboSumCount = (Integer)mapResult.get("comboSumCount");
        List<OrderSetGoods> itemGoodsList =	(List<OrderSetGoods>)singleObj;
        Map<String,List<OrderSetGoods>> comboMap = (Map<String, List<OrderSetGoods>>) comboObj;
        List<PrintBreakFastDto>	singleCountDate = itemGoodsPrint(userId, itemGoodsList);
        List<PrintBreakFastDto>	comboCountDate = comboGoodsPrint(userId, comboMap);
        //int toIndex = printRuleConfig.getSingleMax();
        int toIndex = 5;
        //记录可以组合到套餐中的单品
        List<OrderSetGoods> surplusSingle = new ArrayList<OrderSetGoods>();
        for (PrintBreakFastDto printBreakFastDto : singleCountDate) {
            //找出单品数量小于 阈值的数据
            if(printBreakFastDto.getGoodsCount()<toIndex) {
                //判断套餐能否将多余的单品组合进去
                if((comboCount + printBreakFastDto.getGoodsCount())<=(toIndex*comboSumCount)) {
                    surplusSingle.addAll(printBreakFastDto.getListOrderSetDetail());
                    singleCountDate.remove(printBreakFastDto);
                    break;
                }
            }
        }
        List<OrderSetGoods> surplusSingleGoods = new ArrayList<OrderSetGoods>();
        if(!CollectionUtils.isEmpty(surplusSingle)) {
            //------------------
            for (OrderSetGoods orderSetDetailGoodsDto : surplusSingle) {
                int goodsNumber = orderSetDetailGoodsDto.getGoodsNumber();
                for (int i = 0; i < goodsNumber; i++) {
                    orderSetDetailGoodsDto.setGoodsNumber(1);
                    OrderSetGoods dto = BeanMapper.map(orderSetDetailGoodsDto, OrderSetGoods.class);
                    surplusSingleGoods.add(dto);
                }
            }
            //------------
            for (PrintBreakFastDto printBreakFastDto : comboCountDate) {

                List<OrderSetGoods> surplusSingleRemove = new ArrayList<OrderSetGoods>();
                Map<String,OrderSetGoods> map = new HashMap<String,OrderSetGoods>();
                for (int i = 0; i < surplusSingleGoods.size(); i++) {
                    OrderSetGoods single = surplusSingleGoods.get(i);
                    if(printBreakFastDto.getGoodsCount()+single.getGoodsNumber()<=5) {
                        OrderSetGoods  goodsDto = map.get(single.getGoodsCode());
                        if(null != goodsDto) {
                            goodsDto.setGoodsNumber(goodsDto.getGoodsNumber()+1);
                            map.put(single.getGoodsCode(), goodsDto);
                        }else {
                            map.put(single.getGoodsCode(), single);
                        }
                        surplusSingleRemove.add(single);
                        printBreakFastDto.setGoodsCount(printBreakFastDto.getGoodsCount()+single.getGoodsNumber());
                    }else {
                        break;
                    }
                }
                List<OrderSetGoods> listSurplusSingle = new ArrayList<OrderSetGoods>();
                listSurplusSingle.addAll(map.values());
                printBreakFastDto.setSurplusSingle(listSurplusSingle);
                surplusSingleGoods.removeAll(surplusSingleRemove);
            }
        }
        log.info("处理完成后的数据：组合品数据{}",JSON.toJSONString(comboCountDate));
        log.info("处理完成后的数据：剩余单品数据{}",JSON.toJSONString(surplusSingleGoods));
        log.info("处理完成后的数据：单品数据{}",JSON.toJSONString(singleCountDate));
        allDate.setComboCountDate(comboCountDate);
        allDate.setSingleCountDate(singleCountDate);
        return singleCountDate.size()+comboCountDate.size();
    }


    /**
     **单品处理
     **/
    private List<PrintBreakFastDto> itemGoodsPrint(String userId, List<OrderSetGoods> singleGoodsList) {
        List<PrintBreakFastDto> listPrintDate = new ArrayList<PrintBreakFastDto>();
        //处理单品
        if(!CollectionUtils.isEmpty(singleGoodsList)) {
            List<List<OrderSetGoods>> listGroup = new ArrayList<List<OrderSetGoods>>();
            //子集合的长度
            //int toIndex = printRuleConfig.getSingleMax();
            int toIndex = 5;
            for (OrderSetGoods  goods : singleGoodsList) {
                if(listGroup.size()>0) {
                    int index = listGroup.size();
                    List<OrderSetGoods> listDetail = listGroup.get(index-1);
                    if(listDetail.size() < toIndex) {
                        listDetail.add(goods);
                    }else {
                        List<OrderSetGoods> detailList = new ArrayList<OrderSetGoods>();
                        detailList.add(goods);
                        listGroup.add(detailList);
                    }
                }else {
                    List<OrderSetGoods> detailGroup = new ArrayList<OrderSetGoods>();
                    detailGroup.add(goods);
                    listGroup.add(detailGroup);
                }
            }

            for (List<OrderSetGoods> list : listGroup) {
                //处理集合中相同商品
                log.info("打印前的数据{}",JSON.toJSONString(list));
                Map<String,OrderSetGoods> map = new HashMap<String,OrderSetGoods>();
                for (OrderSetGoods orderSetDetailGoodsDto : list) {
                    OrderSetGoods goods =	map.get(orderSetDetailGoodsDto.getGoodsCode());
                    if(goods != null) {
                        goods.setGoodsNumber(goods.getGoodsNumber()+1);
                    } else {
                        goods = BeanMapper.map(orderSetDetailGoodsDto, OrderSetGoods.class);
                    }
                    map.put(orderSetDetailGoodsDto.getGoodsCode(), goods);
                }
                List<OrderSetGoods> listOrderSetDetail = new ArrayList<OrderSetGoods>();
                listOrderSetDetail.addAll(map.values());
                PrintBreakFastDto printDate = new PrintBreakFastDto();
                Integer singleGoodsSum = listOrderSetDetail.stream().map(OrderSetGoods::getGoodsNumber).reduce(0, (a, b) -> a + b);
                printDate.setListOrderSetDetail(listOrderSetDetail);
                //printDate.setOrderSetPrintMap(orderSetPrintMap);
                printDate.setGoodsCount(singleGoodsSum);
                listPrintDate.add(printDate);
            }
        }

        return listPrintDate;
    }

    /**
     **套餐处理
     **/
    private List<PrintBreakFastDto> comboGoodsPrint(String userId, Map<String,List<OrderSetGoods>> comboMap) {
        List<PrintBreakFastDto> listPrintDate = new ArrayList<PrintBreakFastDto>();
        if(!CollectionUtils.isEmpty(comboMap)) {

            for (String goodsCode : comboMap.keySet()) {
                //组合商品code
                List<OrderSetGoods> listOrderSetDetails = comboMap.get(goodsCode);
                if(!CollectionUtils.isEmpty(listOrderSetDetails)) {
                    OrderSetGoods orderSetDetail = listOrderSetDetails.get(0);
                    for (int i = 0; i < orderSetDetail.getComboGoodsCount(); i++) {
                        Integer singleGoodsSum = listOrderSetDetails.stream().map(OrderSetGoods::getGoodsNumber).reduce(0, (a, b) -> a + b);
                        PrintBreakFastDto printDate = new PrintBreakFastDto();
                        printDate.setListOrderSetDetail(listOrderSetDetails);
                        printDate.setGoodsCount(singleGoodsSum/orderSetDetail.getComboGoodsCount());
                        //printDate.setOrderSetPrintMap(orderSetPrintMap);
                        listPrintDate.add(printDate);
                    }

                }
            }
        }
        return listPrintDate;
    }



}

