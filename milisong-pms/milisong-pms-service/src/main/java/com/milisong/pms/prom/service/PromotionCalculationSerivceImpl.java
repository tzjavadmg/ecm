package com.milisong.pms.prom.service;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.exception.BusinessException;
import com.milisong.pms.prom.api.BreakfastCouponService;
import com.milisong.pms.prom.api.PromotionCalculationSerivce;
import com.milisong.pms.prom.api.UserPointService;
import com.milisong.pms.prom.api.UserRedPacketService;
import com.milisong.pms.prom.domain.UserCoupon;
import com.milisong.pms.prom.dto.*;
import com.milisong.pms.prom.enums.UserPointEnum;
import com.milisong.pms.prom.mapper.UserCouponMapper;
import com.milisong.pms.prom.param.OrderDeliveryGoodsParam;
import com.milisong.pms.prom.param.OrderDeliveryParam;
import com.milisong.pms.prom.param.OrderParam;
import com.milisong.pms.prom.properties.PmsProperties;
import com.milisong.pms.prom.util.UserPointUtil;
import com.milisong.ucs.enums.BusinessLineEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

import static com.milisong.pms.prom.enums.CouponEnum.TYPE.GOODS;
import static com.milisong.pms.prom.enums.PromotionResponseCode.*;
import static com.milisong.pms.prom.enums.UserPointEnum.Action.PAY_ORDER;

/**
 * @author sailor wang
 * @date 2018/11/8 4:05 PM
 * @description
 */
@Slf4j
@Service
@RestController
public class PromotionCalculationSerivceImpl implements PromotionCalculationSerivce {

    @Autowired
    UserRedPacketService userRedPacketService;

    @Autowired
    BreakfastCouponService breakfastCouponService;

    @Autowired
    UserCouponMapper userCouponMapper;

    @Autowired
    CommonService commonService;

    @Autowired
    UserPointService userPointService;

    @Autowired
    PmsProperties pmsProperties;

    /**
     * ????????????????????????????????????????????????????????????????????????
     *
     * @param orderParam ??????????????????
     * @return OrderAmountDto
     */
    @Override
    @PostMapping(value = "/v1/PromotionCalculationSerivce/calculate")
    @Transactional
    public ResponseResult<OrderAmountDto> calculate(@RequestBody OrderParam orderParam) {
        log.info("??????????????????????????????????????? ?????? orderParam -> {}", orderParam);
        if (BusinessLineEnum.BREAKFAST.getCode().equals(orderParam.getBusinessLine())) {
            ResponseResult<OrderAmountDto> result = breakfastCalculate(orderParam);
            //???????????????????????????????????????
            sendUserPointIfRequire(result);
            return result;
        }
        return lunchCalculate(orderParam);
    }

    @Override
    @PostMapping(value = "/v1/PromotionCalculationSerivce/preCalculate")
    public ResponseResult<OrderAmountDto> preCalculate(@RequestBody OrderParam orderParam) {
        log.info("??????????????????????????????????????? ?????? orderParam -> {}", orderParam);
        if (BusinessLineEnum.BREAKFAST.getCode().equals(orderParam.getBusinessLine())) {
            ResponseResult<OrderAmountDto> result = breakfastCalculate(orderParam);
            //???????????????????????????????????????
            sendUserPointIfRequire(result);
            return result;
        }
        return lunchCalculate(orderParam);
    }

    /**
     * ???????????????????????????????????????
     * @param result
     */
    private void sendUserPointIfRequire(ResponseResult<OrderAmountDto> result){
        log.info("--???????????????????????????--{}---{}", JSON.toJSONString(result),pmsProperties.getUserPointSwitch());
        if(!pmsProperties.getUserPointSwitch()){
            if(result != null){
                OrderAmountDto data = result.getData();
                if(data != null){
                    data.setAcquirePoints(0);
                }
            }
        }
        log.info("--????????????--{}", JSON.toJSONString(result));
    }

    private ResponseResult<OrderAmountDto> breakfastCalculate(OrderParam orderParam) {
        try {
            if (orderParam.getTotalGoodsActualAmount() == null || orderParam.getTotalGoodsActualAmount().compareTo(BigDecimal.ZERO) != 1) {
                return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
            }
            //????????????
            BigDecimal goodsActualAmount = orderParam.getTotalGoodsActualAmount();
            //?????????
            BigDecimal packageActualAmount = orderParam.getTotalPackageActualAmount() == null ? BigDecimal.ZERO : orderParam.getTotalPackageActualAmount();

            OrderAmountDto orderAmount = new OrderAmountDto();

            if (orderParam.getCouponId() != null) {
                UserCoupon userCoupon = userCouponMapper.selectByPrimaryKey(orderParam.getCouponId());
                if (userCoupon != null) {
                    if (GOODS.getCode().equals(userCoupon.getType())) {
                        //?????????
                        calcGoodsCoupon(orderAmount, userCoupon, orderParam);
                    } else {
                        //?????????
                        calcDiscountCoupon(orderAmount, userCoupon, orderParam);
                    }
                } else {
                    log.info("??????????????????????????????");
                }
            } else {
                BigDecimal totalAmount = goodsActualAmount.add(packageActualAmount);
                orderAmount.setTotalAmount(totalAmount);
                // ????????????
                Integer point = calcPoint(orderAmount, goodsActualAmount, orderParam);
                log.info("---------------?????????????????? totalAmount -> {}, ???????????? -> {}, ????????????{}", totalAmount, point, packageActualAmount);

            }

            return ResponseResult.buildSuccessResponse(orderAmount);
        } catch (Exception e) {
            log.error("", e);
            return ResponseResult.buildFailResponse(CALC_PAY_AMOUNT_EXCEPTION.code(), CALC_PAY_AMOUNT_EXCEPTION.message());
        }
    }

    private void calcGoodsCoupon(OrderAmountDto orderAmount, UserCoupon userCoupon, OrderParam orderParam) {
        //????????????
        BigDecimal goodsActualAmount = orderParam.getTotalGoodsActualAmount();
        //?????????
        BigDecimal packageActualAmount = orderParam.getTotalPackageActualAmount() == null ? BigDecimal.ZERO : orderParam.getTotalPackageActualAmount();

        List<OrderDeliveryParam> deliveryList = orderParam.getDeliveries();

        if (CollectionUtils.isNotEmpty(deliveryList)) {
            ko:
            for (OrderDeliveryParam orderDelivery : deliveryList) {
                Long deliveryId = orderDelivery.getId();
                if (CollectionUtils.isNotEmpty(orderDelivery.getDeliveryGoods())) {
                    for (OrderDeliveryGoodsParam deliveryGoods : orderDelivery.getDeliveryGoods()) {
                        String goodsCode = deliveryGoods.getGoodsCode();
                        if (userCoupon.getGoodsCode().equalsIgnoreCase(goodsCode)) {
                            BigDecimal actualPrice = deliveryGoods.getGoodsActualPrice();
                            // ????????? = ????????? - ???????????? + x??????
                            BigDecimal goodsAmount = goodsActualAmount.add(userCoupon.getDiscount()).subtract(actualPrice);
                            orderAmount.setTotalAmount(goodsAmount.add(packageActualAmount));
                            orderAmount.setCouponDiscount(userCoupon.getDiscount());//?????????
                            orderAmount.setCouponAmount(actualPrice.subtract(userCoupon.getDiscount()));//?????????????????????
                            orderAmount.setCouponGoodsCode(goodsCode);//????????????
                            orderAmount.setCouponType(userCoupon.getType());
                            orderAmount.setDeliveryId(deliveryId);//??????id

                            //????????????
                            Integer point = calcPoint(orderAmount, goodsAmount, orderParam);
                            log.info("??????????????????????????? totalAmount -> {}??? ???????????? -> {} ???????????? -> {}, ????????????{}", orderAmount.getTotalAmount(), orderAmount.getCouponAmount(), point, packageActualAmount);
                            break ko;
                        }
                    }
                }
            }
        }

        if (StringUtils.isEmpty(orderAmount.getCouponGoodsCode())) {
            //????????????????????????
            BigDecimal totalAmount = goodsActualAmount.add(packageActualAmount);
            log.info("---------------???????????????????????? , ?????????????????? totalAmount -> {}, ????????????{}", totalAmount, packageActualAmount);
            orderAmount.setTotalAmount(totalAmount);
        }
    }

    /**
     * ?????????
     *
     * @param orderAmount
     * @param userCoupon
     * @param orderParam
     */
    private void calcDiscountCoupon(OrderAmountDto orderAmount, UserCoupon userCoupon, OrderParam orderParam) {
        //????????????
        BigDecimal goodsActualAmount = orderParam.getTotalGoodsActualAmount();
        //?????????
        BigDecimal packageActualAmount = orderParam.getTotalPackageActualAmount() == null ? BigDecimal.ZERO : orderParam.getTotalPackageActualAmount();

        // ???????????? x ??????
        BigDecimal saleAmount = (goodsActualAmount.multiply(userCoupon.getDiscount()).divide(new BigDecimal(10))).setScale(2, BigDecimal.ROUND_HALF_UP);
        orderAmount.setCouponType(userCoupon.getType());
        orderAmount.setTotalAmount(saleAmount.add(packageActualAmount));
        orderAmount.setCouponDiscount(userCoupon.getDiscount());
        orderAmount.setCouponAmount(goodsActualAmount.subtract(saleAmount).setScale(2, BigDecimal.ROUND_HALF_UP));
        //????????????
        Integer usePoint = calcPoint(orderAmount, saleAmount, orderParam);
        log.info("?????????????????? totalAmount -> {}??? ???????????? -> {}, ???????????? -> {} ????????????{}", saleAmount.add(packageActualAmount), goodsActualAmount.subtract(saleAmount).setScale(2, BigDecimal.ROUND_HALF_UP), usePoint, packageActualAmount);
    }


    /**
     * ????????????
     *
     * @param orderAmount
     * @param goodsAmount
     * @param orderParam
     */
    private Integer calcPoint(OrderAmountDto orderAmount, BigDecimal goodsAmount, OrderParam orderParam) {
        // ????????????
        Integer deductionPoints = orderParam.getDeductionPoints() == null ? 0 : orderParam.getDeductionPoints();

        //??????????????????
        orderAmount.setDeductionPointsAmount(BigDecimal.ZERO);
        // ????????????????????????
        orderAmount.setAcquirePoints(goodsAmount.setScale(0, BigDecimal.ROUND_CEILING).intValue());
        //???????????????????????????
        orderAmount.setUsePoints(0);

        if (deductionPoints > 0) {// ????????????
            // ?????????
            BigDecimal packageActualAmount = orderParam.getTotalPackageActualAmount() == null ? BigDecimal.ZERO : orderParam.getTotalPackageActualAmount();
            int score = goodsAmount.multiply(new BigDecimal(UserPointEnum.POINT_RATE)).intValue();
            if (deductionPoints > score) {
                String ex = "??????????????????????????? -> " + deductionPoints + ", ???????????????????????? -> " + score;
                throw new BusinessException(GREAT_THAN_ACTUAL_AMOUNT.code(), GREAT_THAN_ACTUAL_AMOUNT.message(), ex);
            }
            BigDecimal deductionPointsAmount = new BigDecimal(deductionPoints / UserPointEnum.POINT_RATE).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal payAmount = goodsAmount.subtract(deductionPointsAmount);
            orderAmount.setTotalAmount(payAmount.add(packageActualAmount));

            orderAmount.setUsePoints(deductionPoints);
            orderAmount.setDeductionPointsAmount(deductionPointsAmount);
            orderAmount.setAcquirePoints(payAmount.setScale(0, BigDecimal.ROUND_CEILING).intValue());

            UserPointWaterDto userPointWater = new UserPointWaterDto();
            userPointWater.setUserId(orderParam.getUserId());
            userPointWater.setIncomeType(UserPointEnum.IncomeType.OUT.getCode());
            userPointWater.setRefType(UserPointEnum.RefType.ORDER.getCode());
            userPointWater.setRefId(orderParam.getId());
            userPointWater.setAction(PAY_ORDER.getCode());
            userPointWater.setBusinessLine(orderParam.getBusinessLine());
            userPointWater.setPoint(deductionPoints);
            userPointWater.setRemark(UserPointUtil.pointRemark(PAY_ORDER,deductionPoints));

            ResponseResult<Boolean> result = userPointService.saveUserPointWater(userPointWater);
            if (!result.isSuccess()) {
                throw new BusinessException(POINT_SAVE_FAIL.code(), POINT_SAVE_FAIL.message(), POINT_SAVE_FAIL.message());
            }
        }
        return deductionPoints;
    }


    private ResponseResult<OrderAmountDto> lunchCalculate(OrderParam orderParam) {
        try {
            if (orderParam == null || orderParam.getTotalGoodsActualAmount() == null || orderParam.getTotalGoodsActualAmount().compareTo(BigDecimal.ZERO) != 1) {
                return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
            }
            BigDecimal goodsActualAmount = orderParam.getTotalGoodsActualAmount(); //????????????

            BigDecimal packageActualAmount = orderParam.getTotalPackageActualAmount() == null ? BigDecimal.ZERO : orderParam.getTotalPackageActualAmount(); //????????????

            OrderAmountDto orderAmount = new OrderAmountDto();
            orderAmount.setTotalAmount(goodsActualAmount.add(packageActualAmount));// ????????????+????????????
            log.info("????????????+???????????? totalAmount -> {}", orderAmount.getTotalAmount());


            //?????????????????????
            ResponseResult<FullReduce> result = userRedPacketService.fullReduceConfig();
            log.info("???????????? -> {}", result.getData());
            if (result.isSuccess() && result.getData() != null) {
                List<FullReducePair> fullReducePairList = result.getData().getFullReducePairList();
                for (int i = fullReducePairList.size() - 1; i >= 0; i--) {
                    FullReducePair fullReducePair = fullReducePairList.get(i);
                    if (orderAmount.getTotalAmount().compareTo(fullReducePair.getFull()) >= 0) {
                        log.info("??? -> {}, ??? -> {}", fullReducePair.getFull(), fullReducePair.getReduce());
                        orderAmount.setFullReduceAmount(fullReducePair.getReduce());
                        orderAmount.setTotalAmount(orderAmount.getTotalAmount().subtract(fullReducePair.getReduce()));
                        break;
                    }
                }

            }

            //?????????
            Long redPacketId = orderParam.getRedPacketId();
            if (redPacketId != null) {
                ResponseResult<UserRedPacketDto> responseResult = userRedPacketService.queryByUserRedPacketId(redPacketId);
                log.info("???????????? -> {}", responseResult.getData());
                if (responseResult.isSuccess() && responseResult.getData() != null) {
                    BigDecimal redpacketAmount = responseResult.getData().getAmount();
                    log.info("???????????? -> {}", redpacketAmount);
                    orderAmount.setRedPackAmount(redpacketAmount);
                    orderAmount.setTotalAmount(orderAmount.getTotalAmount().subtract(redpacketAmount));
                }
            }

            return ResponseResult.buildSuccessResponse(orderAmount);
        } catch (Exception e) {
            log.error("", e);
            return ResponseResult.buildFailResponse(CALC_PAY_AMOUNT_EXCEPTION.code(), CALC_PAY_AMOUNT_EXCEPTION.message());
        }
    }


}