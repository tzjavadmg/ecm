package com.milisong.pms.prom.mq;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.distrib.LockProvider;
import com.milisong.oms.api.OrderService;
import com.milisong.oms.constant.DeliveryStatus;
import com.milisong.oms.dto.OrderDeliveryDto;
import com.milisong.oms.dto.OrderDto;
import com.milisong.pms.prom.annotation.RecordMQ;
import com.milisong.pms.prom.api.UserPointService;
import com.milisong.pms.prom.domain.ActivityUserInvite;
import com.milisong.pms.prom.domain.UserCoupon;
import com.milisong.pms.prom.domain.UserInviteRecord;
import com.milisong.pms.prom.dto.UserPointWaterDto;
import com.milisong.pms.prom.enums.CouponEnum;
import com.milisong.pms.prom.enums.MQRecordType;
import com.milisong.pms.prom.enums.UserInviteEnum;
import com.milisong.pms.prom.enums.UserPointEnum;
import com.milisong.pms.prom.mapper.ActivityUserInviteMapper;
import com.milisong.pms.prom.mapper.UserCouponMapper;
import com.milisong.pms.prom.mapper.UserInviteRecordMapper;
import com.milisong.pms.prom.properties.PmsProperties;
import com.milisong.pms.prom.util.GlobalConfigCacheUtil;
import com.milisong.pms.prom.util.UserPointUtil;
import com.milisong.ucs.enums.BusinessLineEnum;
import com.sun.javafx.binding.StringFormatter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.Message;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 消费订单完成信息
 *
 * @author tianhaibo
 * @version 1.0.0
 * @date 2018/12/19 14:18
 */
@Slf4j
@EnableBinding(MessageSink.class)
@MessageEndpoint(value = "promotionOrderCompleteConsumer")
public class OrderCompleteConsumer {

    @Autowired
    private UserPointService userPointService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserInviteRecordMapper recordMapper;

    @Autowired
    private ActivityUserInviteMapper inviteMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private PmsProperties pmsProperties;

    //配送完成状态时4
    private static final Byte COMPLATE_STATUS = new Byte("4");

    @RecordMQ(MQRecordType.POINT)
    @StreamListener(MessageSink.MESSAGE_ORDER_STATUS_INPUT)
    public void accept(Message<String> message) {
        log.info("促销系统==========接收订单状态变更MQ消息，参数={}", JSONObject.toJSONString(message));
        OrderMqDto orderMqDto = JSONObject.parseObject(message.getPayload(),OrderMqDto.class);
        if(!orderMqDto.getStatus().equals(COMPLATE_STATUS)) {
            return;
        }
        ResponseResult<OrderDto> responseResult = orderService.getOrderByDeliveryNo(orderMqDto.getOrderNo());
        OrderDto data = responseResult.getData();
        afterComplateSendPoint(data,orderMqDto.getOrderNo());
        afterComplateSendInviteCoupon(data,orderMqDto.getOrderNo());
    }

    /**
     * 订单完成配送后，送给邀请者免费券
     * @param deliveryNo
     * @param data
     */
    private void afterComplateSendInviteCoupon(OrderDto data,String deliveryNo){
        if(data == null ){
            log.error("----接收订单状态变更MQ消息--没有查询到订单详细信息");
            return;
        }
        if(!data.getOrderType().equals(BusinessLineEnum.BREAKFAST.getCode())){
            log.info("---不是早餐，不处理邀新送券业务---");
            return;
        }
        //1.检查此配送单是否是已完成状态
        List<OrderDeliveryDto> deliveries = data.getDeliveries();
        OrderDeliveryDto item = null;
        for (OrderDeliveryDto o:deliveries) {
            if(o.getDeliveryNo().equals(deliveryNo)){
                item = o;
                break;
            }
        }
        if(item == null ){
            return;
        }
        //2.配送已完成，检查此用户对应的邀新记录状态是否为1.在路上
        UserInviteRecord userInviteRecord = recordMapper.queryByReceiveUserId(data.getUserId());
        if(userInviteRecord == null){
            return;
        }
        Byte inviteStatus = userInviteRecord.getStatus();
        //只有记录信息是1.在路上 状态时，才执行更新，其他状态都不处理
        if(!inviteStatus.equals(UserInviteEnum.INVITE_RECORD_STATUS.ON_DELIVERY.getValue())){
            return;
        }
        //3.检查邀新主体时间是否已结束
        ActivityUserInvite activityUserInvite = inviteMapper.selectByPrimaryKey(userInviteRecord.getActivityInviteId());
        if(activityUserInvite == null){
            return;
        }
        Date endDate = activityUserInvite.getEndDate();
        if(endDate == null || endDate.before(new Date())){
            log.info("该邀新实体已结束,标记为邀请失败，设置成功时间，实体id={}",userInviteRecord.getActivityInviteId());
            //更新状态为邀新成功,不送券
            UserInviteRecord record = new UserInviteRecord();
            record.setId(userInviteRecord.getId());
            record.setInviteSuccessTime(new Date());
            record.setStatus(UserInviteEnum.INVITE_RECORD_STATUS.FAILD.getValue());
            record.setRemark(UserInviteEnum.INVITE_RECORD_REMARK.INVITE_OVER.getDesc());
            recordMapper.updateByPrimaryKeySelective(record);
            return;
        }
        //没结束，检查是否超过数量，加锁,防止针对邀请者计算是并发
        RLock lock = LockProvider.getLock("invite_info:"+userInviteRecord.getOriginateUserId().toString());
        try {
            lock.lock(30, TimeUnit.SECONDS);//1分钟
            //duble check状态
            UserInviteRecord tempUserInviteRecord = recordMapper.queryByReceiveUserId(data.getUserId());
            if(tempUserInviteRecord == null){
                return;
            }
            Byte tempInviteStatus = tempUserInviteRecord.getStatus();
            if(!tempInviteStatus.equals(UserInviteEnum.INVITE_RECORD_STATUS.ON_DELIVERY.getValue())){
                return;
            }
            //4.检查邀请者的邀新成功数量是否已超过限制值
            Integer count = recordMapper.sumByActivityInviteIdAndStatus(userInviteRecord.getActivityInviteId(), UserInviteEnum.INVITE_RECORD_STATUS.SUCCESS.getValue());
            UserInviteRecord updateDto = new UserInviteRecord();
            updateDto.setId(userInviteRecord.getId());
            if(count!= null && count.compareTo(activityUserInvite.getMaxCount()) >= 0){
                log.info("----邀新获得券已达到上限,不送券,标记为失败，设置成功时间");
                updateDto.setInviteSuccessTime(new Date());
                updateDto.setStatus(UserInviteEnum.INVITE_RECORD_STATUS.FAILD.getValue());
                updateDto.setRemark(UserInviteEnum.INVITE_RECORD_REMARK.MAX_LIMIT.getDesc());
                recordMapper.updateByPrimaryKeySelective(updateDto);
            }else{
                //5.更新券记录状态为成功
                updateDto.setInviteSuccessTime(new Date());
                updateDto.setStatus(UserInviteEnum.INVITE_RECORD_STATUS.SUCCESS.getValue());
                updateDto.setRemark(UserInviteEnum.INVITE_RECORD_REMARK.SUCCESS.getDesc());
                int result = recordMapper.updateByPrimaryKeySelective(updateDto);
                log.info("---更新邀新记录表数据状态成功old-{},new-{},开始发券",inviteStatus,updateDto.getStatus());
                //TODO 6.发券
                if(result > 0){
                    receiveCoupon(activityUserInvite,tempUserInviteRecord);
                }
            }
        }catch (Exception e){
            log.error("送券失败",e);
        }finally {
            lock.unlock();
        }


    }

    /**
     * 处理积分业务
     * @param data
     */
    private void afterComplateSendPoint(OrderDto data,String deliveryNo) {
        //关闭赠送积分
        Boolean userPointOpenStatus = pmsProperties.getUserPointSwitch();
        if(!userPointOpenStatus){
            return;
        }
        if(data == null ){
            log.error("----接收订单状态变更MQ消息--没有查询到订单详细信息--");
            return;
        }
        if(!data.getOrderType().equals(BusinessLineEnum.BREAKFAST.getCode())){
            log.info("---不是早餐，不处理积分---");
            return;
        }
        BigDecimal sumMoney = calcOrderSum(data, deliveryNo);
        if(sumMoney == null){
            return;
        }
        UserPointWaterDto userPointWaterDto = UserPointWaterDto.builder()
                .action(UserPointEnum.Action.FINISH_ORDER.getCode())
                .businessLine(data.getOrderType())
                .incomeType(UserPointEnum.IncomeType.IN.getCode())
                .point(UserPointUtil.money2Point(sumMoney))
                .refId(data.getId())
                .refType(UserPointEnum.RefType.ORDER.getCode())
                .userId(data.getUserId())
                .remark(StringFormatter.format(UserPointUtil.POINT_COMPLATE,sumMoney).getValue())
                .build();
        userPointService.saveUserPointWater(userPointWaterDto);
    }

    private static BigDecimal calcOrderSum(OrderDto data, String deliveryNo){
        log.info("----查询订单数据---{}", JSONObject.toJSONString(data));
        List<OrderDeliveryDto> deliveries = data.getDeliveries();
        if(deliveries == null || deliveries.size() == 0){
            return null;
        }
        for (OrderDeliveryDto o:deliveries) {
            //忽略本单
            if(o.getDeliveryNo().equals(deliveryNo)){
                continue;
            }
            if(!o.getStatus().equals(DeliveryStatus.CANCELED.getValue())&&
                    !o.getStatus().equals(DeliveryStatus.REFUNDED.getValue())&&
                    !o.getStatus().equals(DeliveryStatus.COMPLETED.getValue())){
                return null;
            }
        }
        //计算应得积分
        BigDecimal sumPointAmount = BigDecimal.ZERO;
        for (OrderDeliveryDto o:deliveries) {
            if(o.getDeliveryNo().equals(deliveryNo)
                    ||o.getStatus().equals(DeliveryStatus.COMPLETED.getValue())){
                sumPointAmount = sumPointAmount.add(o.getShareOrderPayAmount());
                if(o.getDeliveryActualPrice()!=null){
                    sumPointAmount = sumPointAmount.subtract(o.getDeliveryActualPrice());
                }
                if(o.getTotalPackageActualAmount()!=null){
                    sumPointAmount = sumPointAmount.subtract(o.getTotalPackageActualAmount());
                }
            }
        }
        log.info("----计算总金额---{}",sumPointAmount);
        BigDecimal amount = data.getTotalPayAmount();
        if(sumPointAmount.compareTo(amount)>0){
            log.error("计算订单积分异常，子单金额总和大于主单金额-主单-"+data.getId()+",主单金额-"+amount+",子单总金额-"+sumPointAmount);
            return null;
        }
        return sumPointAmount;
    }

    private UserCoupon receiveCoupon(ActivityUserInvite invite ,UserInviteRecord inviteRecord) {
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setId(IdGenerator.nextId());
        userCoupon.setActivityId(GlobalConfigCacheUtil.getInviteActivityId());
        userCoupon.setUserActivityId(invite.getId());
        userCoupon.setName(inviteRecord.getCouponName());
        userCoupon.setType(CouponEnum.TYPE.GOODS.getCode());
        userCoupon.setUserId(invite.getOriginateUserId());
        userCoupon.setDiscount(inviteRecord.getDiscount());//
        userCoupon.setExpireTime(sumExpireTime(invite.getValidDays()));//
        userCoupon.setIsShare((byte)0);
        userCoupon.setIsValid((byte) 1);
        userCoupon.setIsUsed((byte) 0);
        String inviteGoodsInfo = GlobalConfigCacheUtil.getInviteGoodsInfo();
        if(StringUtils.isNotEmpty(inviteGoodsInfo)){
            JSONObject jsonObject = JSONObject.parseObject(inviteGoodsInfo);
            String rule = jsonObject.getString("rule");
            Integer days = jsonObject.getInteger("days");
            String goodsCode = jsonObject.getString("goodsCode");
            userCoupon.setRule(rule);
            userCoupon.setDays(days);//满几天可用
            userCoupon.setGoodsCode(goodsCode);
        }
        userCoupon.setBusinessLine(BusinessLineEnum.BREAKFAST.getCode());
        userCoupon.setCreateTime(new Date());
        userCoupon.setUpdateTime(new Date());
        userCouponMapper.insertSelective(userCoupon);
        return userCoupon;
    }

    private Date sumExpireTime(Integer days){
        DateTime validDate = new DateTime().plusDays(days);
        Date expireTime = new DateTime(validDate.getYear(), validDate.getMonthOfYear(), validDate.getDayOfMonth(), 23, 59, 59).toDate();
        return expireTime;
    }

    @Getter
    @Setter
    public static class OrderMqDto {
        // 订单编号
        private String orderNo;
        // 完成日期
        private Date date;
        // 集单状态 1待打包 2已打包 3配送中 4已通知
        private Byte status;

        //顺丰状态
        private Byte sfStatus;
    }
}

