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
     * 分析可参与的促销活动，计算订单实际需要支付的金额
     *
     * @param orderParam 订单信息参数
     * @return OrderAmountDto
     */
    @Override
    @PostMapping(value = "/v1/PromotionCalculationSerivce/calculate")
    @Transactional
    public ResponseResult<OrderAmountDto> calculate(@RequestBody OrderParam orderParam) {
        log.info("计算订单实际需要支付的金额 入参 orderParam -> {}", orderParam);
        if (BusinessLineEnum.BREAKFAST.getCode().equals(orderParam.getBusinessLine())) {
            ResponseResult<OrderAmountDto> result = breakfastCalculate(orderParam);
            //根据积分开关判断是否送积分
            sendUserPointIfRequire(result);
            return result;
        }
        return lunchCalculate(orderParam);
    }

    @Override
    @PostMapping(value = "/v1/PromotionCalculationSerivce/preCalculate")
    public ResponseResult<OrderAmountDto> preCalculate(@RequestBody OrderParam orderParam) {
        log.info("计算订单实际需要支付的金额 入参 orderParam -> {}", orderParam);
        if (BusinessLineEnum.BREAKFAST.getCode().equals(orderParam.getBusinessLine())) {
            ResponseResult<OrderAmountDto> result = breakfastCalculate(orderParam);
            //根据积分开关判断是否送积分
            sendUserPointIfRequire(result);
            return result;
        }
        return lunchCalculate(orderParam);
    }

    /**
     * 根据积分开关判断是否送积分
     * @param result
     */
    private void sendUserPointIfRequire(ResponseResult<OrderAmountDto> result){
        log.info("--开始处理是否送积分--{}---{}", JSON.toJSONString(result),pmsProperties.getUserPointSwitch());
        if(!pmsProperties.getUserPointSwitch()){
            if(result != null){
                OrderAmountDto data = result.getData();
                if(data != null){
                    data.setAcquirePoints(0);
                }
            }
        }
        log.info("--处理完毕--{}", JSON.toJSONString(result));
    }

    private ResponseResult<OrderAmountDto> breakfastCalculate(OrderParam orderParam) {
        try {
            if (orderParam.getTotalGoodsActualAmount() == null || orderParam.getTotalGoodsActualAmount().compareTo(BigDecimal.ZERO) != 1) {
                return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
            }
            //商品金额
            BigDecimal goodsActualAmount = orderParam.getTotalGoodsActualAmount();
            //包装费
            BigDecimal packageActualAmount = orderParam.getTotalPackageActualAmount() == null ? BigDecimal.ZERO : orderParam.getTotalPackageActualAmount();

            OrderAmountDto orderAmount = new OrderAmountDto();

            if (orderParam.getCouponId() != null) {
                UserCoupon userCoupon = userCouponMapper.selectByPrimaryKey(orderParam.getCouponId());
                if (userCoupon != null) {
                    if (GOODS.getCode().equals(userCoupon.getType())) {
                        //商品券
                        calcGoodsCoupon(orderAmount, userCoupon, orderParam);
                    } else {
                        //折扣券
                        calcDiscountCoupon(orderAmount, userCoupon, orderParam);
                    }
                } else {
                    log.info("没有匹配优惠券！！！");
                }
            } else {
                BigDecimal totalAmount = goodsActualAmount.add(packageActualAmount);
                orderAmount.setTotalAmount(totalAmount);
                // 计算积分
                Integer point = calcPoint(orderAmount, goodsActualAmount, orderParam);
                log.info("---------------最终支付价格 totalAmount -> {}, 使用积分 -> {}, 打包费：{}", totalAmount, point, packageActualAmount);

            }

            return ResponseResult.buildSuccessResponse(orderAmount);
        } catch (Exception e) {
            log.error("", e);
            return ResponseResult.buildFailResponse(CALC_PAY_AMOUNT_EXCEPTION.code(), CALC_PAY_AMOUNT_EXCEPTION.message());
        }
    }

    private void calcGoodsCoupon(OrderAmountDto orderAmount, UserCoupon userCoupon, OrderParam orderParam) {
        //商品金额
        BigDecimal goodsActualAmount = orderParam.getTotalGoodsActualAmount();
        //包装费
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
                            // 总金额 = 总金额 - 商品原价 + x元购
                            BigDecimal goodsAmount = goodsActualAmount.add(userCoupon.getDiscount()).subtract(actualPrice);
                            orderAmount.setTotalAmount(goodsAmount.add(packageActualAmount));
                            orderAmount.setCouponDiscount(userCoupon.getDiscount());//券金额
                            orderAmount.setCouponAmount(actualPrice.subtract(userCoupon.getDiscount()));//优惠了多少金额
                            orderAmount.setCouponGoodsCode(goodsCode);//商品编码
                            orderAmount.setCouponType(userCoupon.getType());
                            orderAmount.setDeliveryId(deliveryId);//集单id

                            //计算积分
                            Integer point = calcPoint(orderAmount, goodsAmount, orderParam);
                            log.info("商品券最终支付价格 totalAmount -> {}， 优惠金额 -> {} 使用积分 -> {}, 打包费：{}", orderAmount.getTotalAmount(), orderAmount.getCouponAmount(), point, packageActualAmount);
                            break ko;
                        }
                    }
                }
            }
        }

        if (StringUtils.isEmpty(orderAmount.getCouponGoodsCode())) {
            //没有匹配的商品券
            BigDecimal totalAmount = goodsActualAmount.add(packageActualAmount);
            log.info("---------------没有匹配的商品券 , 最终支付价格 totalAmount -> {}, 打包费：{}", totalAmount, packageActualAmount);
            orderAmount.setTotalAmount(totalAmount);
        }
    }

    /**
     * 折扣券
     *
     * @param orderAmount
     * @param userCoupon
     * @param orderParam
     */
    private void calcDiscountCoupon(OrderAmountDto orderAmount, UserCoupon userCoupon, OrderParam orderParam) {
        //商品金额
        BigDecimal goodsActualAmount = orderParam.getTotalGoodsActualAmount();
        //包装费
        BigDecimal packageActualAmount = orderParam.getTotalPackageActualAmount() == null ? BigDecimal.ZERO : orderParam.getTotalPackageActualAmount();

        // 商品总价 x 折扣
        BigDecimal saleAmount = (goodsActualAmount.multiply(userCoupon.getDiscount()).divide(new BigDecimal(10))).setScale(2, BigDecimal.ROUND_HALF_UP);
        orderAmount.setCouponType(userCoupon.getType());
        orderAmount.setTotalAmount(saleAmount.add(packageActualAmount));
        orderAmount.setCouponDiscount(userCoupon.getDiscount());
        orderAmount.setCouponAmount(goodsActualAmount.subtract(saleAmount).setScale(2, BigDecimal.ROUND_HALF_UP));
        //计算积分
        Integer usePoint = calcPoint(orderAmount, saleAmount, orderParam);
        log.info("最终支付价格 totalAmount -> {}， 优惠金额 -> {}, 使用积分 -> {} 打包费：{}", saleAmount.add(packageActualAmount), goodsActualAmount.subtract(saleAmount).setScale(2, BigDecimal.ROUND_HALF_UP), usePoint, packageActualAmount);
    }


    /**
     * 计算积分
     *
     * @param orderAmount
     * @param goodsAmount
     * @param orderParam
     */
    private Integer calcPoint(OrderAmountDto orderAmount, BigDecimal goodsAmount, OrderParam orderParam) {
        // 抵扣积分
        Integer deductionPoints = orderParam.getDeductionPoints() == null ? 0 : orderParam.getDeductionPoints();

        //积分抵扣金额
        orderAmount.setDeductionPointsAmount(BigDecimal.ZERO);
        // 订单获取多少积分
        orderAmount.setAcquirePoints(goodsAmount.setScale(0, BigDecimal.ROUND_CEILING).intValue());
        //本次使用了多少积分
        orderAmount.setUsePoints(0);

        if (deductionPoints > 0) {// 用了积分
            // 包装费
            BigDecimal packageActualAmount = orderParam.getTotalPackageActualAmount() == null ? BigDecimal.ZERO : orderParam.getTotalPackageActualAmount();
            int score = goodsAmount.multiply(new BigDecimal(UserPointEnum.POINT_RATE)).intValue();
            if (deductionPoints > score) {
                String ex = "本次使用了多少积分 -> " + deductionPoints + ", 实际商品对应积分 -> " + score;
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
            BigDecimal goodsActualAmount = orderParam.getTotalGoodsActualAmount(); //商品金额

            BigDecimal packageActualAmount = orderParam.getTotalPackageActualAmount() == null ? BigDecimal.ZERO : orderParam.getTotalPackageActualAmount(); //打包费用

            OrderAmountDto orderAmount = new OrderAmountDto();
            orderAmount.setTotalAmount(goodsActualAmount.add(packageActualAmount));// 商品总价+包装总费
            log.info("商品总价+包装总费 totalAmount -> {}", orderAmount.getTotalAmount());


            //是否有满减活动
            ResponseResult<FullReduce> result = userRedPacketService.fullReduceConfig();
            log.info("满减活动 -> {}", result.getData());
            if (result.isSuccess() && result.getData() != null) {
                List<FullReducePair> fullReducePairList = result.getData().getFullReducePairList();
                for (int i = fullReducePairList.size() - 1; i >= 0; i--) {
                    FullReducePair fullReducePair = fullReducePairList.get(i);
                    if (orderAmount.getTotalAmount().compareTo(fullReducePair.getFull()) >= 0) {
                        log.info("满 -> {}, 减 -> {}", fullReducePair.getFull(), fullReducePair.getReduce());
                        orderAmount.setFullReduceAmount(fullReducePair.getReduce());
                        orderAmount.setTotalAmount(orderAmount.getTotalAmount().subtract(fullReducePair.getReduce()));
                        break;
                    }
                }

            }

            //有红包
            Long redPacketId = orderParam.getRedPacketId();
            if (redPacketId != null) {
                ResponseResult<UserRedPacketDto> responseResult = userRedPacketService.queryByUserRedPacketId(redPacketId);
                log.info("红包数据 -> {}", responseResult.getData());
                if (responseResult.isSuccess() && responseResult.getData() != null) {
                    BigDecimal redpacketAmount = responseResult.getData().getAmount();
                    log.info("红包金额 -> {}", redpacketAmount);
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