package com.mili.oss.service;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.util.BeanMapper;
import com.mili.oss.api.OrderService;
import com.mili.oss.api.OrderSetService;
import com.mili.oss.constant.OrderTypeEnum;
import com.mili.oss.domain.OrderSet;
import com.mili.oss.domain.OrderSetExample;
import com.mili.oss.domain.OrderSetGoods;
import com.mili.oss.domain.OrderSetGoodsExample;
import com.mili.oss.dto.*;
import com.mili.oss.dto.param.OrderSearchParamPos;
import com.mili.oss.mapper.OrderSetGoodsMapper;
import com.mili.oss.mapper.OrderSetMapper;

import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhaozhonghui
 * @Description 集单接口实现
 * @date 2019-02-25
 */
@Slf4j
@Service("orderSetService")
@RestController
public class OrderSetServiceImpl implements OrderSetService {

    @Autowired
    private OrderSetMapper orderSetMapper;
    @Autowired
    private OrderSetGoodsMapper orderSetGoodsMapper;
    @Autowired
    private OrderService orderService;

    @Override
    public ResponseResult<Pagination<OrderSetDetailResultDto>> pageSearchOrderSet(@RequestBody OrderSetReqDto param) {
        if (null == param.getOrderType()) {
            param.setOrderType(OrderTypeEnum.LUNCH.getCode());
        }
        if (completeCoupletNo(param)) {
            return ResponseResult.buildFailResponse("9999","单号传入格式不对，请传入单号后几位数字！");
        }
        long count = orderSetMapper.selectCount(param);
        List<OrderSetDetailDto> list = orderSetMapper.selectPage(param);
        Pagination<OrderSetDetailResultDto> pagination = new Pagination<>();
        List<String> detailSetNos = list.stream().map(OrderSetDetailDto::getDetailSetNo).collect(Collectors.toList());
        Map<String, Integer> resultMap = getCustomerSumByListSetNo(detailSetNos);
        List<OrderSetDetailResultDto> resultList = BeanMapper.mapList(list, OrderSetDetailResultDto.class);
        resultList.stream().forEach(item -> {
            item.setCustomerSum(resultMap.get(item.getDetailSetNo()));
        });
        pagination.setDataList(resultList);
        pagination.setTotalCount(count);
        pagination.setPageSize(param.getPageSize());
        pagination.setPageNo(param.getPageNo());
        return ResponseResult.buildSuccessResponse(pagination);
    }

    @Override
    public ResponseResult<List<DistributionOrdersetInfoResult>> getCustomerOrderByOrderSetNo(@RequestParam("setNo") String setNo) {
        log.info("根据集单号查询{}", setNo);
        List<DistributionOrdersetInfoResult> listResult = orderSetGoodsMapper.getCustomerOrderByOrderSetNo(setNo);
        Map<String, DistributionOrdersetInfoResult> mapResult = new TreeMap<String, DistributionOrdersetInfoResult>();
        for (DistributionOrdersetInfoResult distributionOrdersetInfoResult : listResult) {
            mapResult.put(distributionOrdersetInfoResult.getCoupletNo(), distributionOrdersetInfoResult);
        }
        for (DistributionOrdersetInfoResult distributionOrdersetInfoResult : listResult) {
            DistributionOrdersetInfoResult detail = mapResult.get(distributionOrdersetInfoResult.getCoupletNo());
            if (detail != null) {
                OrderSetDetailGoodsDto goodsInfo = new OrderSetDetailGoodsDto();
                goodsInfo.setGoodsName(distributionOrdersetInfoResult.getGoodsName());
                goodsInfo.setGoodsNumber(distributionOrdersetInfoResult.getGoodsNumber());
                List<OrderSetDetailGoodsDto> listGoods = detail.getGoodsInfo();
                if (CollectionUtils.isEmpty(listGoods)) {
                    listGoods = new ArrayList<OrderSetDetailGoodsDto>();
                }
                listGoods.add(goodsInfo);
                detail.setGoodsInfo(listGoods);
            }
        }
        listResult.clear();
        listResult.addAll(mapResult.values());
        return ResponseResult.buildSuccessResponse(listResult);
    }

    @Override
    public ResponseResult<List<OrderSetDetailDto>> getOrderSetByOrderNo(@RequestParam("orderNo") String orderNo) {
        List<OrderSetDetailDto> orderSetDetailDtoList = new ArrayList<>();
        OrderSetGoodsExample goodsExample = new OrderSetGoodsExample();
        goodsExample.createCriteria().andOrderNoEqualTo(orderNo);
        List<OrderSetGoods> orderSetDetailGoods = orderSetGoodsMapper.selectByExample(goodsExample);
        if (orderSetDetailGoods.size() > 0) {
            orderSetDetailGoods.stream().forEach(orderSetGoods -> {
                String detailSetNo = orderSetGoods.getDetailSetNo();
                OrderSetExample example = new OrderSetExample();
                example.createCriteria().andDetailSetNoEqualTo(detailSetNo);
                List<OrderSet> orderSetDetails = orderSetMapper.selectByExample(example);
                OrderSet orderSetDetail = orderSetDetails.get(0);
                OrderSetDetailDto orderSetDetailDto = BeanMapper.map(orderSetDetail, OrderSetDetailDto.class);
                orderSetDetailDtoList.add(orderSetDetailDto);
            });
        }
        return ResponseResult.buildSuccessResponse(orderSetDetailDtoList);
    }

    @Override
    public ResponseResult<NotifyOrderSetQueryResult> getDetailSetInfoByDetailNo(@RequestParam(name = "detailSetNo", required = false) String detailSetNo, @RequestParam(name = "detailSetId", required = false) Long detailSetId) {
        if (StringUtils.isBlank(detailSetNo) && null == detailSetId) {
            return null;
        }
        OrderSetExample detailExample = new OrderSetExample();
        OrderSetExample.Criteria criteria = detailExample.createCriteria().andIsDeletedEqualTo(false);
        if (StringUtils.isNotBlank(detailSetNo)) {
            criteria.andDetailSetNoEqualTo(detailSetNo);
        }
        if (null != detailSetId) {
            criteria.andIdEqualTo(detailSetId);
        }
        List<OrderSet> detailList = orderSetMapper.selectByExample(detailExample);
        if (CollectionUtils.isEmpty(detailList)) {
            return null;
        }
        detailSetNo = detailList.get(0).getDetailSetNo();

        OrderSetGoodsExample goodsExample = new OrderSetGoodsExample();
        goodsExample.createCriteria().andDetailSetNoEqualTo(detailSetNo).andIsDeletedEqualTo(false);
        List<OrderSetGoods> goodsList = orderSetGoodsMapper.selectByExample(goodsExample);

        NotifyOrderSetQueryResult result = new NotifyOrderSetQueryResult();

        result.setDistributionNo(detailList.get(0).getDetailSetNoDescription());
        if (!CollectionUtils.isEmpty(goodsList)) {
            List<OrderDetailResult> orderDetailList = orderService.getOrderDetailInfoByOrderNo(
                    goodsList.stream().map(item -> item.getOrderNo()).collect(Collectors.toList()));
            result.setListOrderDetails(orderDetailList);
        }
        result.setOrderSet(BeanMapper.map(detailList.get(0), OrderSetDetailDto.class));
        result.setGoodsList(BeanMapper.mapList(goodsList, OrderSetDetailGoodsDto.class));

        return ResponseResult.buildSuccessResponse(result);
    }

    public Map<String, Integer> getCustomerSumByListSetNo(List<String> list) {
        log.info("根据集单号查询有多少顾客单{}", JSON.toJSONString(list));
        Map<String, Integer> resultMap = new HashMap<String, Integer>();
        if (CollectionUtils.isEmpty(list)) {
            return resultMap;
        }
        List<Map<String, Object>> listMap = orderSetGoodsMapper.selectCountCustomerOrderBySetNo(list);
        for (Map<String, Object> map : listMap) {
            resultMap.put(map.get("detail_set_no").toString(), Integer.parseInt(map.get("coupletNo").toString()));
        }
        return resultMap;
    }

    @Override
    public NotifyOrderSetQueryResult getOrderSetMqBySetNo(@RequestParam("setNo") String setNo) {
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
        return result;
    }

    @Override
    public OrderSetProductionMsgByPrint getBFOrderSetMqBySetNo(String setNo) {
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
        return result;
    }
    
    // 补全顾客联、配送单号
    private boolean completeCoupletNo(@RequestBody OrderSetReqDto param) {
        
        String description = param.getDescription();
        if (!StringUtil.isBlank(description)) {
            try {
                description = String.valueOf(Integer.valueOf(description));
            } catch (Exception e) {
                log.warn("取餐号格式化失败:{}", description);
                return true;
            }
            if (param.getOrderType() != null) {
                String prefix = "B"; // 早餐F 午餐B
                if (param.getOrderType().equals(OrderTypeEnum.BREAKFAST.getCode())) {
                    prefix = "F";
                }
                //取餐号格式 F0002
                description = buildCoupleNo(description, prefix);
                param.setDescription(description);
            } else {
                List<String> descriptionList = new ArrayList<>();
                param.setDescription(null);
                String bfDescription = description;
                // 午餐
                String prefix = "B";
                description = buildCoupleNo(description, prefix);
                descriptionList.add(description);
                // 早餐
                prefix = "F";
                bfDescription = buildCoupleNo(bfDescription, prefix);
                descriptionList.add(bfDescription);

                param.setDescriptionList(descriptionList);
            }

        }
        return false;
    }
    
    private String buildCoupleNo(String bfCoupletNo, String prefix) {
        String bfSrtFormat = prefix.concat("%04d");
        bfCoupletNo = String.format(bfSrtFormat, Integer.valueOf(bfCoupletNo));
        return bfCoupletNo;
    }
}
