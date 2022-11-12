package com.milisong.scm.printer.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.BooleanUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.util.BeanMapper;
import com.gp.port.Drive;
import com.milisong.scm.printer.Template.jiabo.GainschaBillTemplateByTsc;
import com.milisong.scm.printer.Template.jiabo.GainschaBillTemplateByTscAndBreakfast;
import com.milisong.scm.printer.api.PosPrintService;
import com.milisong.scm.printer.dto.OrderDetailInfo;
import com.milisong.scm.printer.dto.PrintAllBreakFastDto;
import com.milisong.scm.printer.dto.PrintBreakFastDto;
import com.milisong.scm.printer.dto.PrintLunchDto;
import com.milisong.scm.printer.properties.PrintRuleConfig;
import com.milisong.scm.printer.request.mq.OrderSetDetailDto;
import com.milisong.scm.printer.request.mq.OrderSetDetailGoodsDto;
import com.milisong.scm.printer.request.mq.OrderSetProductionMsg;
import com.milisong.scm.printer.util.jiabo.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
*@author    created by benny
*@date  2018年10月26日---下午8:03:53
*
*/
@Slf4j
@Service
public class PosPrintServiceImpl implements PosPrintService {

	@Autowired
	private PrintRuleConfig printRuleConfig;
	// 前台打印机
	private static Drive receptionPrintDrive = null;
	static {
		receptionPrintDrive = new Drive();
		//boolean b = dr.opendrive("GP-H80300 Series");
		boolean b = receptionPrintDrive.opendrive("ZH3080(标签)");
		if (b)
			log.info("前台打印机准备就绪[receptionPrintDrive---ok]");
			else {
				log.info("前台打印机准备失败[printDrive---false]");
				//receptionPrintDrive.opendrive("ZH3080(标签)");
			}
	}
	
	@Autowired
	GainschaBillTemplateByTsc template;
	
	@Autowired
	GainschaBillTemplateByTscAndBreakfast templateBreakfast;
	
	private List<String> breakfastSetNo = new ArrayList<String>();
	private List<String> setNo = new ArrayList<String>();
	@Override
	public void printOrdeSet(String msg,Integer printType) {
		log.info("打印午餐");
		log.info("开始处理需要打印的数据{}",msg);
		OrderSetProductionMsg msgDto = JSONObject.parseObject(msg, OrderSetProductionMsg.class);
		OrderSetDetailDto orderSetDetailDto = msgDto.getOrderSet();
		if(printType == -1) {
			if(setNo.contains(orderSetDetailDto.getDetailSetNo())) {
				log.info("集单{}已打印过、无需再打印",orderSetDetailDto.getDetailSetNo());
				return ;
			}
			if(StringUtils.checkSetNo(orderSetDetailDto.getDetailSetNo())){
				log.info("非当天集单、不进行打印{}",orderSetDetailDto.getDetailSetNo());
				return;
			}
		}
		orderSetDetailDto.setDetailDeliveryAddress(orderSetDetailDto.getDetailDeliveryAddress()+orderSetDetailDto.getCompanyAbbreviation());
		Map<String,Object> orderSetPrintMap = getPrintOrderSetInfoBy(orderSetDetailDto);
		List<OrderDetailInfo> listCustomerGoodsInfo = new ArrayList<OrderDetailInfo>();
		Map<String,List<OrderSetDetailGoodsDto>> customerMap = new TreeMap<String,List<OrderSetDetailGoodsDto>>();
		Map<String,Integer> orderGoodsCountMap = new HashMap<String,Integer>();
		for (OrderSetDetailGoodsDto orderSetGoodsInfo : msgDto.getGoods()) {
			//统计所有商品
			Integer count = orderGoodsCountMap.get(orderSetGoodsInfo.getGoodsName());
			if(null == count) {
				count = 0;
			}
			count +=orderSetGoodsInfo.getGoodsNumber();
			orderGoodsCountMap.put(orderSetGoodsInfo.getGoodsName(), count);
			//组装集单联中顾客商品信息
			OrderDetailInfo orderInfo = new OrderDetailInfo();
			orderInfo.setName(orderSetGoodsInfo.getUserName());
			orderInfo.setGoodsName(orderSetGoodsInfo.getGoodsName());
			orderInfo.setPhone(orderSetGoodsInfo.getUserPhone());
			orderInfo.setGoodsCount(orderSetGoodsInfo.getGoodsNumber());
			listCustomerGoodsInfo.add(orderInfo);
			// 客户联数据组装
			String coupletNo = orderSetGoodsInfo.getCoupletNo();
			List<OrderSetDetailGoodsDto> listOrderInfo = customerMap.get(coupletNo);
			if(CollectionUtils.isEmpty(listOrderInfo)) {
				listOrderInfo = new ArrayList<OrderSetDetailGoodsDto>();
			}
			listOrderInfo.add(orderSetGoodsInfo);
			customerMap.put(coupletNo, listOrderInfo);
		 }
		 orderSetPrintMap.put("customerGoodsInfo", listCustomerGoodsInfo);
		 
		 //前台打印机操作
		 Drive d1 = new Drive();
		 d1.opendrive("ZH3080(标签)");
		
		 int copiesNumber = 0 ;
		
		 Map<String,List<OrderSetDetailGoodsDto>> picklesMap = new HashMap<String,List<OrderSetDetailGoodsDto>>();
		 Map<String,List<PrintLunchDto>> printLunchMap = new TreeMap<String,List<PrintLunchDto>>();
		 for (String userId : customerMap.keySet()) {
			 	List<PrintLunchDto> printLunch = new ArrayList<PrintLunchDto>();
				List<OrderSetDetailGoodsDto> listCustomer = customerMap.get(userId);
				for (OrderSetDetailGoodsDto orderSetDetailGoodsDto : listCustomer) {
					
					//非主餐的商品根据订单号单独存放
					if(orderSetDetailGoodsDto.getType() != (byte)0) {
						List<OrderSetDetailGoodsDto> listpick =	picklesMap.get(userId);
						if(CollectionUtils.isEmpty(listpick)) {
							listpick = Lists.newArrayList();
						}
						listpick.add(orderSetDetailGoodsDto);
						picklesMap.put(userId, listpick);
					}else {
						//主餐
						Integer goodsCount = orderSetDetailGoodsDto.getGoodsNumber();
						for (int i = 0; i < goodsCount; i++) {
							List<OrderSetDetailGoodsDto> customerInfo = new ArrayList<OrderSetDetailGoodsDto>();
							orderSetDetailGoodsDto.setGoodsNumber(1);
							customerInfo.add(orderSetDetailGoodsDto);
							PrintLunchDto p = new PrintLunchDto();
							p.setCoupletNo(userId);
							p.setCustomerInfo(customerInfo);
							p.setOrderSetPrintMap(orderSetPrintMap);
							copiesNumber++;
							printLunch.add(p);
						}
					}
				}
				printLunchMap.put(userId, printLunch);
		}
		 int i = 0;
		 //将小菜 存放在任意顾客联中
		 for (String userId : printLunchMap.keySet()) {
			 List<PrintLunchDto> listlunch = printLunchMap.get(userId);
			 List<OrderSetDetailGoodsDto> listUserIdPick = picklesMap.get(userId);
			 if(!CollectionUtils.isEmpty(listUserIdPick)) {
				 //顾客没有点主餐
				 if(CollectionUtils.isEmpty(listlunch)) {
					 	listlunch = Lists.newArrayList();
					 	PrintLunchDto p = new PrintLunchDto();
						p.setCoupletNo(userId);
						p.setCustomerInfo(listUserIdPick);
						p.setOrderSetPrintMap(orderSetPrintMap);
						copiesNumber++;
						listlunch.add(p);
						printLunchMap.put(userId, listlunch);
				 }else {
					 //顾客点了主餐
					 listlunch.get(0).getCustomerInfo().addAll(listUserIdPick);
				 }
			 }
			 
			 
		}
		 for (List<PrintLunchDto> orderDetailInfo : printLunchMap.values()) {
			 for (PrintLunchDto printLunchDto : orderDetailInfo) {
				 i ++; //张数计算
				 doPrintLunch(printType, msgDto, printLunchDto, d1, i,copiesNumber);
			}
		}
		
		 orderSetPrintMap.put("copiesNumber", copiesNumber);
		 
		 if(printType == -1 || printType ==1 || printType == 0) {
			 byte[] orderInfo = template.setOrderTemplate(orderSetPrintMap);
			 log.info("打印{}配送单",orderSetPrintMap.get("setNo"));
			 d1.sendMessage(orderInfo);
		 }
		 //后台打印机操作
//		 Drive d2 = new Drive();
//		 d2.opendrive("ZH308I(标签)");
//		 SimpleDateFormat smp = new SimpleDateFormat("HH:mm");
//		 String deliveryTime = smp.format(orderSetDetailDto.getDeliveryStartTime())+"~"+smp.format(orderSetDetailDto.getDeliveryEndTime());
//		 byte[] prodcutionInfo = template.printProductionUnion(orderGoodsCountMap, deliveryTime,orderSetDetailDto.getDetailSetNoDescription());
//		 d2.sendMessage(prodcutionInfo);
		 setNo.add(orderSetDetailDto.getDetailSetNo());
	}

 

	private void doPrintLunch(Integer printType, OrderSetProductionMsg msgDto, PrintLunchDto printLunchDto,Drive d1, int i,
			int copiesNumber) {
		byte[]  customer = template.customerTemplate(printLunchDto.getCustomerInfo(),printLunchDto.getOrderSetPrintMap(),i,copiesNumber);
		log.info("打印{}顾客单",printLunchDto.getCoupletNo());
		if(msgDto.getCoupletNo()!= null &&printLunchDto.getCoupletNo().equals(msgDto.getCoupletNo())) {
			if(printType == 2) {
				d1.sendMessage(customer);
			}
		}else if(msgDto.getCoupletNo()==null ){
			d1.sendMessage(customer);
		}else if(printType == 0) { //等于 是页面打印全部的操作
			d1.sendMessage(customer);
		}
	}
	
	private Map<String,Object> getPrintOrderSetInfoBy(OrderSetDetailDto orderSetDetailDto){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("setNo", orderSetDetailDto.getDetailSetNoDescription());
		SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");
		map.put("deliveryDate", simp.format(orderSetDetailDto.getDetailDeliveryDate()));
		map.put("buildingAbbreviation", orderSetDetailDto.getBuildingAbbreviation());
		String detailDeliveryAddress = orderSetDetailDto.getDetailDeliveryAddress();
		String address = detailDeliveryAddress.substring(0, detailDeliveryAddress.length()-orderSetDetailDto.getCompanyAbbreviation().length());
		map.put("detailDeliveryAddress", address+orderSetDetailDto.getDeliveryFloor()+"楼");
		map.put("company", orderSetDetailDto.getCompanyAbbreviation());
		map.put("mealAddress", orderSetDetailDto.getMealAddress()==null?"":orderSetDetailDto.getMealAddress());
		 //增加配送开始、结束时间
		SimpleDateFormat simpTime = new SimpleDateFormat("HH:mm");
		map.put("deliveryTime", simpTime.format(orderSetDetailDto.getDeliveryStartTime()) +"--"+simpTime.format(orderSetDetailDto.getDeliveryEndTime()));
		return map;
	}

	@Override
	public void printOrdeBreakfastSet(String msg,Integer printType) {
		log.info("打印早餐");
		log.info("开始处理需要打印的数据{}",msg);
		OrderSetProductionMsg msgDto = JSONObject.parseObject(msg, OrderSetProductionMsg.class);
		OrderSetDetailDto orderSetDetailDto = msgDto.getOrderSet();
		//只有系统打印类型才要进行订单重复校验
		if(printType == -1) {
			if(breakfastSetNo.contains(orderSetDetailDto.getDetailSetNo())) {
				log.info("集单{}已打印过、无需再打印",orderSetDetailDto.getDetailSetNo());
				return ;
			}
			if(StringUtils.checkSetNo(orderSetDetailDto.getDetailSetNo())){
				log.info("非当天集单、不进行打印{}",orderSetDetailDto.getDetailSetNo());
				return;
			}
		}
		orderSetDetailDto.setDetailDeliveryAddress(orderSetDetailDto.getDetailDeliveryAddress()+orderSetDetailDto.getCompanyAbbreviation());
		Map<String,Object> orderSetPrintMap = getPrintOrderSetInfoBy(orderSetDetailDto);
		List<OrderDetailInfo> listCustomerGoodsInfo = new ArrayList<OrderDetailInfo>();
		Map<String,List<OrderSetDetailGoodsDto>> customerMap = new TreeMap<String,List<OrderSetDetailGoodsDto>>();
		Map<String,OrderDetailInfo> mapOrderDetailInfo = new HashMap<String, OrderDetailInfo>();
		Map<String,Integer> orderGoodsCountMap = new HashMap<String,Integer>();
		for (OrderSetDetailGoodsDto orderSetGoodsInfo : msgDto.getGoods()) {
			//统计所有商品
			Integer count = orderGoodsCountMap.get(orderSetGoodsInfo.getGoodsName());
			if(null == count) {
				count = 0;
			}
			count +=orderSetGoodsInfo.getGoodsNumber();
			orderGoodsCountMap.put(orderSetGoodsInfo.getGoodsName(), count);
			//组装集单联中顾客商品信息
			OrderDetailInfo orderInfo = new OrderDetailInfo();
			orderInfo.setName(orderSetGoodsInfo.getUserName());
			orderInfo.setGoodsName(orderSetGoodsInfo.getGoodsName());
			orderInfo.setPhone(orderSetGoodsInfo.getUserPhone());
			orderInfo.setGoodsCount(orderSetGoodsInfo.getGoodsNumber());
			OrderDetailInfo orderDetailInfo = mapOrderDetailInfo.get(orderSetGoodsInfo.getUserName());
			if(orderDetailInfo == null ) {
				listCustomerGoodsInfo.add(orderInfo);
			}
			mapOrderDetailInfo.put(orderSetGoodsInfo.getUserName(),orderInfo);
			// 客户联数据组装
			String coupletNo = orderSetGoodsInfo.getCoupletNo();
			List<OrderSetDetailGoodsDto> listOrderInfo = customerMap.get(coupletNo);
			if(CollectionUtils.isEmpty(listOrderInfo)) {
				listOrderInfo = new ArrayList<OrderSetDetailGoodsDto>();
			}
			listOrderInfo.add(orderSetGoodsInfo);
			customerMap.put(coupletNo, listOrderInfo);
			
		 }
		 orderSetPrintMap.put("customerGoodsInfo", listCustomerGoodsInfo);
		 
		 
		 //前台打印机操作
		 Drive d1 = new Drive();
		 d1.opendrive("ZH3080(标签)");
	
		 int copiesNumber = 0 ;
		
		 copiesNumber =  pritnCustomer(orderSetPrintMap, customerMap, d1,printType,msgDto);
		 orderSetPrintMap.put("copiesNumber", copiesNumber);
		 if(printType == -1 || printType ==1 || printType == 0) {
			 byte[] orderInfo = templateBreakfast.setOrderTemplate(orderSetPrintMap);
			 log.info("打印{}配送单",orderSetPrintMap.get("setNo"));
			 d1.sendMessage(orderInfo);
			 }
		 
		 log.info("记录打印的集单号:setNo:{}",orderSetDetailDto.getDetailSetNo());
		 if(printType == -1) {
			 breakfastSetNo.add(orderSetDetailDto.getDetailSetNo());
		 }
	}

	/**
	 * 打印顾客联
	 * @param orderSetPrintMap
	 * @param customerMap
	 * @param d1
	 * @param msgDto 
	 * @param msgDto 
	 */
	private int pritnCustomer(Map<String, Object> orderSetPrintMap,
			Map<String, List<OrderSetDetailGoodsDto>> customerMap, Drive d1,Integer printType, OrderSetProductionMsg msgDto) {
		log.info("顾客联信息{}",JSON.toJSONString(customerMap));
		int number = 0;
		List<PrintAllBreakFastDto> listAllDate = new ArrayList<PrintAllBreakFastDto>();
		 for (String userId : customerMap.keySet()) {
				List<OrderSetDetailGoodsDto> listCustomer = customerMap.get(userId);
				Map<String, Object> mapResult = getCustomerDateComb(listCustomer);
				Object singleObj = mapResult.get("single");
				Object comboObj = mapResult.get("combo");
				log.info("处理单品和 套餐的数据{}",JSON.toJSONString(mapResult));
				int singleCount = 0;
				int comboCount = 0;
				List<OrderSetDetailGoodsDto> singleGoodsList =	(List<OrderSetDetailGoodsDto>)singleObj;
				Map<String,List<OrderSetDetailGoodsDto>> comboMap = (Map<String, List<OrderSetDetailGoodsDto>>) comboObj;
				//单品和套餐都有的情况下
				PrintAllBreakFastDto allDate = new PrintAllBreakFastDto();
				allDate.setCoupletNo(userId);
				if(!CollectionUtils.isEmpty(singleGoodsList) && ! CollectionUtils.isEmpty(comboMap) ) {
					number += goodsPrintForBreakFast(orderSetPrintMap, d1, userId, mapResult, printType,allDate);
				}else {
					List<PrintBreakFastDto>	singleCountDate = singleGoodsPrint(orderSetPrintMap, d1, userId, singleGoodsList,printType);
					singleCount = singleCountDate.size();
					List<PrintBreakFastDto>	comboCountDate = comboGoodsPrint(orderSetPrintMap, d1, userId, comboMap,printType);
					comboCount = comboCountDate.size();
					
					allDate.setComboCountDate(comboCountDate);
					allDate.setSingleCountDate(singleCountDate);
					
				//	doPrintCustomer(d1, printType, userId, singleCountDate, comboCountDate);
				}
				listAllDate.add(allDate);
				number += singleCount + comboCount;
				log.info("顾客:{},一共{}份",userId,number);
		 }
		 Integer single = 0;
		 for (PrintAllBreakFastDto printAllBreakFastDto : listAllDate) {
			 single = doPrintCustomer(d1, printType,  printAllBreakFastDto.getSingleCountDate(), printAllBreakFastDto.getComboCountDate(),number,single,msgDto,printAllBreakFastDto.getCoupletNo());
		}
		 log.info("总顾客数量{}，总份数{}",customerMap.keySet().size(),number);
		 return number;
	}

	private int  doPrintCustomer(Drive d1, Integer printType, List<PrintBreakFastDto> singleCountDate,
			List<PrintBreakFastDto> comboCountDate,int count, Integer single, OrderSetProductionMsg msgDto, String coupletNo) {
		if(printType == -1 || printType ==2 || printType == 0) {
			 if(!CollectionUtils.isEmpty(singleCountDate)) {
				 for (PrintBreakFastDto printBreakFastDto : singleCountDate) {
					 single++;
					 byte[]  customer = null;
					 if(org.apache.commons.lang3.StringUtils.isBlank(msgDto.getCoupletNo())) {
						 customer = templateBreakfast.customerTemplate(printBreakFastDto.getListOrderSetDetail(),printBreakFastDto.getOrderSetPrintMap(),single,count);
					 }else if(coupletNo.equals(msgDto.getCoupletNo())){
						 customer = templateBreakfast.customerTemplate(printBreakFastDto.getListOrderSetDetail(),printBreakFastDto.getOrderSetPrintMap(),single,count);
					 }else if("default".equals(msgDto.getCoupletNo())) {
						 // 打印全部、
						 customer = templateBreakfast.customerTemplate(printBreakFastDto.getListOrderSetDetail(),printBreakFastDto.getOrderSetPrintMap(),single,count);
					 }
					 if(null != customer) {
						 d1.sendMessage(customer);
					 }
					 log.info("开始打印第：{} 份",single);
				 }
			 }
			 if(!CollectionUtils.isEmpty(comboCountDate)) {
				 for (PrintBreakFastDto printBreakFastDto : comboCountDate) {
					 single++;
					 byte[]  customer = null;
					 if(org.apache.commons.lang3.StringUtils.isBlank(msgDto.getCoupletNo())) {
						 customer = templateBreakfast.customerTemplateCombo(printBreakFastDto.getListOrderSetDetail(),printBreakFastDto.getOrderSetPrintMap(),printBreakFastDto.getSurplusSingle(),single,count);
					 }else if(coupletNo.equals(msgDto.getCoupletNo())){
						 customer = templateBreakfast.customerTemplateCombo(printBreakFastDto.getListOrderSetDetail(),printBreakFastDto.getOrderSetPrintMap(),printBreakFastDto.getSurplusSingle(),single,count);
					 }else if("default".equals(msgDto.getCoupletNo())) {
						 customer = templateBreakfast.customerTemplateCombo(printBreakFastDto.getListOrderSetDetail(),printBreakFastDto.getOrderSetPrintMap(),printBreakFastDto.getSurplusSingle(),single,count);
					 }
					 if(null != customer) {
						 d1.sendMessage(customer);
					 }
					 log.info("开始打印第：{} 份",single);
				 }
			 }
		}
		return single;
	}
	
	
	private int goodsPrintForBreakFast(Map<String, Object> orderSetPrintMap, Drive d1, String userId, Map<String, Object> mapResult,Integer printType, PrintAllBreakFastDto allDate) {
		Object singleObj = mapResult.get("single");
		Object comboObj = mapResult.get("combo");
		Integer comboCount = (Integer)mapResult.get("comboCount"); 
		Integer comboSumCount = (Integer)mapResult.get("comboSumCount");
		List<OrderSetDetailGoodsDto> singleGoodsList =	(List<OrderSetDetailGoodsDto>)singleObj;
		Map<String,List<OrderSetDetailGoodsDto>> comboMap = (Map<String, List<OrderSetDetailGoodsDto>>) comboObj;
		List<PrintBreakFastDto>	singleCountDate = singleGoodsPrint(orderSetPrintMap, d1, userId, singleGoodsList,printType);
		List<PrintBreakFastDto>	comboCountDate = comboGoodsPrint(orderSetPrintMap, d1, userId, comboMap,printType);
		int toIndex = printRuleConfig.getSingleMax();
		//记录可以组合到套餐中的单品
		List<OrderSetDetailGoodsDto> surplusSingle = new ArrayList<OrderSetDetailGoodsDto>();
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
		List<OrderSetDetailGoodsDto> surplusSingleGoods = new ArrayList<OrderSetDetailGoodsDto>();
		if(!CollectionUtils.isEmpty(surplusSingle)) {
			//------------------
			for (OrderSetDetailGoodsDto orderSetDetailGoodsDto : surplusSingle) {
				int goodsNumber = orderSetDetailGoodsDto.getGoodsNumber();
				for (int i = 0; i < goodsNumber; i++) {
					orderSetDetailGoodsDto.setGoodsNumber(1);
					OrderSetDetailGoodsDto dto = BeanMapper.map(orderSetDetailGoodsDto, OrderSetDetailGoodsDto.class);
					surplusSingleGoods.add(dto);
				}
			}
			//------------
			for (PrintBreakFastDto printBreakFastDto : comboCountDate) {
				
				List<OrderSetDetailGoodsDto> surplusSingleRemove = new ArrayList<OrderSetDetailGoodsDto>();
				Map<String,OrderSetDetailGoodsDto> map = new HashMap<String,OrderSetDetailGoodsDto>();
				for (int i = 0; i < surplusSingleGoods.size(); i++) {
					OrderSetDetailGoodsDto single = surplusSingleGoods.get(i);
					if(printBreakFastDto.getGoodsCount()+single.getGoodsNumber()<=5) {
						OrderSetDetailGoodsDto  goodsDto = map.get(single.getGoodsCode());
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
				List<OrderSetDetailGoodsDto> listSurplusSingle = new ArrayList<OrderSetDetailGoodsDto>();
				listSurplusSingle.addAll(map.values());
				printBreakFastDto.setSurplusSingle(listSurplusSingle);
				surplusSingleGoods.removeAll(surplusSingleRemove);
			}
		}
		log.info("处理完成后的数据：组合品数据{}",JSON.toJSONString(comboCountDate));
		log.info("处理完成后的数据：剩余单品数据{}",JSON.toJSONString(surplusSingleGoods));
		log.info("处理完成后的数据：单品数据{}",JSON.toJSONString(singleCountDate));
		//doPrintCustomer(d1, printType, userId, singleCountDate, comboCountDate);
		allDate.setComboCountDate(comboCountDate);
		allDate.setSingleCountDate(singleCountDate);
		return singleCountDate.size()+comboCountDate.size();
	}
	

	private List<PrintBreakFastDto> comboGoodsPrint(Map<String, Object> orderSetPrintMap, Drive d1, String userId, Map<String,List<OrderSetDetailGoodsDto>> comboMap,Integer printType) {
		List<PrintBreakFastDto> listPrintDate = new ArrayList<PrintBreakFastDto>();
		if(!CollectionUtils.isEmpty(comboMap)) {
			
			for (String goodsCode : comboMap.keySet()) {
				//组合商品code
				List<OrderSetDetailGoodsDto> listOrderSetDetails = comboMap.get(goodsCode);
				if(!CollectionUtils.isEmpty(listOrderSetDetails)) {
					OrderSetDetailGoodsDto orderSetDetail = listOrderSetDetails.get(0);
					for (int i = 0; i < orderSetDetail.getComboGoodsCount(); i++) {
						Integer singleGoodsSum = listOrderSetDetails.stream().map(OrderSetDetailGoodsDto::getGoodsNumber).reduce(0, (a, b) -> a + b);
						 PrintBreakFastDto printDate = new PrintBreakFastDto();
						 printDate.setListOrderSetDetail(listOrderSetDetails);
						 printDate.setGoodsCount(singleGoodsSum/orderSetDetail.getComboGoodsCount());
						 printDate.setOrderSetPrintMap(orderSetPrintMap);
				         listPrintDate.add(printDate);
					}
					
				}
			}
		}
		return listPrintDate;
	}

	private List<PrintBreakFastDto> singleGoodsPrint(Map<String, Object> orderSetPrintMap, Drive d1, String userId, List<OrderSetDetailGoodsDto> singleGoodsList,Integer printType) {
		List<PrintBreakFastDto> listPrintDate = new ArrayList<PrintBreakFastDto>();
			//处理单品
			if(!CollectionUtils.isEmpty(singleGoodsList)) {
				List<List<OrderSetDetailGoodsDto>> listGroup = new ArrayList<List<OrderSetDetailGoodsDto>>();
		        //子集合的长度
		        int toIndex = printRuleConfig.getSingleMax();
		        for (OrderSetDetailGoodsDto  goods : singleGoodsList) {
		        	if(listGroup.size()>0) {
		        		int index = listGroup.size();
		        		List<OrderSetDetailGoodsDto> listDetail = listGroup.get(index-1);
		        		if(listDetail.size() < toIndex) {
		        			listDetail.add(goods);
		        		}else {
		        			List<OrderSetDetailGoodsDto> detailList = new ArrayList<OrderSetDetailGoodsDto>();
							detailList.add(goods);
							listGroup.add(detailList);
		        		}
		        	}else {
		        		List<OrderSetDetailGoodsDto> detailGroup = new ArrayList<OrderSetDetailGoodsDto>();
		        		detailGroup.add(goods);
		        		listGroup.add(detailGroup);
		        	}
				}
		     
		        for (List<OrderSetDetailGoodsDto> list : listGroup) {
		        	//处理集合中相同商品
		        	log.info("打印前的数据{}",JSON.toJSONString(list));
		        	Map<String,OrderSetDetailGoodsDto> map = new HashMap<String,OrderSetDetailGoodsDto>();
		        		for (OrderSetDetailGoodsDto orderSetDetailGoodsDto : list) {
		        			OrderSetDetailGoodsDto goods =	map.get(orderSetDetailGoodsDto.getGoodsCode());
		        			if(goods != null) {
		        				goods.setGoodsNumber(goods.getGoodsNumber()+1);
		        			} else {
		        				goods = BeanMapper.map(orderSetDetailGoodsDto, OrderSetDetailGoodsDto.class);
		        			}
		        			map.put(orderSetDetailGoodsDto.getGoodsCode(), goods);
		        		}
		        	List<OrderSetDetailGoodsDto> listOrderSetDetail = new ArrayList<OrderSetDetailGoodsDto>();
		        	listOrderSetDetail.addAll(map.values());
		        	PrintBreakFastDto printDate = new PrintBreakFastDto();
		        	Integer singleGoodsSum = listOrderSetDetail.stream().map(OrderSetDetailGoodsDto::getGoodsNumber).reduce(0, (a, b) -> a + b);
		        	printDate.setListOrderSetDetail(listOrderSetDetail);
		        	printDate.setOrderSetPrintMap(orderSetPrintMap);
		        	printDate.setGoodsCount(singleGoodsSum);
		        	listPrintDate.add(printDate);
				}
			}
			
		return listPrintDate;
	}
	
	private Map<String, Object> getCustomerDateComb(List<OrderSetDetailGoodsDto> orderSetDetailGoodsDto) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Map<String,List<OrderSetDetailGoodsDto>> comboMap = new HashMap<String,List<OrderSetDetailGoodsDto>>();
		List<OrderSetDetailGoodsDto> listSingleCode = new ArrayList<OrderSetDetailGoodsDto>();
		Integer comboCount = 0; //组合品中单品总数量
		Integer singleCount = 0;
		Integer comboSumCount = 0;   //套餐总数量
		for (OrderSetDetailGoodsDto orderSetDetailGoodsDto2 : orderSetDetailGoodsDto) {
			if(BooleanUtils.isNotFalse(orderSetDetailGoodsDto2.getIsCombo())) {
				String comboGoodsCode = orderSetDetailGoodsDto2.getComboGoodsCode();
				List<OrderSetDetailGoodsDto> list = comboMap.get(comboGoodsCode);
				if(CollectionUtils.isEmpty(list)) {
					list = new ArrayList<OrderSetDetailGoodsDto>();
					comboSumCount += orderSetDetailGoodsDto2.getComboGoodsCount();
				}
				comboCount +=orderSetDetailGoodsDto2.getGoodsNumber();
				list.add(orderSetDetailGoodsDto2);
				comboMap.put(comboGoodsCode, list);
			}else {
				if(orderSetDetailGoodsDto2.getGoodsNumber() >= 1) {
					for (int i = 1; i <= orderSetDetailGoodsDto2.getGoodsNumber(); i++) {
						OrderSetDetailGoodsDto orderSetDetailGppds = BeanMapper.map(orderSetDetailGoodsDto2, OrderSetDetailGoodsDto.class);
						orderSetDetailGppds.setGoodsNumber(1);
						listSingleCode.add(orderSetDetailGppds);
					}
				}else {
					listSingleCode.add(orderSetDetailGoodsDto2);
				}
				singleCount += orderSetDetailGoodsDto2.getGoodsNumber();
			}
		}
		resultMap.put("combo", comboMap);
		resultMap.put("single", listSingleCode);
		resultMap.put("comboCount", comboCount);
		resultMap.put("comboSumCount", comboSumCount);
		resultMap.put("singleCount", singleCount);
		return resultMap;
	}
}
