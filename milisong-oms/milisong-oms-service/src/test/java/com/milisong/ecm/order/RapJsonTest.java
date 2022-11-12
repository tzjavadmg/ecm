package com.milisong.ecm.order;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.milisong.oms.dto.*;
import com.milisong.oms.param.*;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/13 10:19
 */
public class RapJsonTest {
    @Test
    public void genVirtualOrderParamJson() {
        VirtualOrderParam virtualOrderParam=new VirtualOrderParam();
        virtualOrderParam.setBuildingId(123456L);
        virtualOrderParam.setShopCode("31010001");
        virtualOrderParam.setConfirm(true);

        VirtualOrderDeliveryParam virtualOrderDeliveryParam=new VirtualOrderDeliveryParam();
        virtualOrderDeliveryParam.setDeliveryDate(new Date());
        virtualOrderParam.setDeliveries(Lists.newArrayList(virtualOrderDeliveryParam));

        VirtualOrderDeliveryGoodsParam virtualOrderDeliveryGoodsParam=new VirtualOrderDeliveryGoodsParam();
        virtualOrderDeliveryGoodsParam.setGoodsCode("5615118281");
        virtualOrderDeliveryGoodsParam.setGoodsCount(1);
        virtualOrderDeliveryParam.setDeliveryGoods(Lists.newArrayList(virtualOrderDeliveryGoodsParam));

        System.out.println(JSON.toJSONString(virtualOrderParam));
    }

    @Test
    public void genVirtualOrderDtoJson() {

        VirtualOrderDto virtualOrderDto = new VirtualOrderDto();
        virtualOrderDto.setId(1521682721538048L);
        virtualOrderDto.setTotalGoodsOriginalAmount(BigDecimal.valueOf(24));
        virtualOrderDto.setTotalGoodsActualAmount(BigDecimal.valueOf(18));
        virtualOrderDto.setTotalDeliveryOriginalAmount(BigDecimal.valueOf(4));
        virtualOrderDto.setTotalDeliveryActualAmount(BigDecimal.valueOf(2));
        virtualOrderDto.setTotalPackageOriginalAmount(BigDecimal.valueOf(3));
        virtualOrderDto.setTotalPackageActualAmount(BigDecimal.valueOf(1));
        virtualOrderDto.setTotalGoodsCount(1);
        virtualOrderDto.setTotalPayAmount(BigDecimal.valueOf(21L));
        virtualOrderDto.setTotalOrderDays(1);
//        virtualOrderDto.setDeliveryStartTime("11:00");
//        virtualOrderDto.setDeliveryEndTime("12:00");
        virtualOrderDto.setOrderDate(DateFormatUtils.format(new Date(),"yyyy-MM-dd"));


        VirtualOrderDeliveryDto virtualOrderDeliveryDto=new VirtualOrderDeliveryDto();
        virtualOrderDeliveryDto.setDeliveryDate("2018-10-08");
        virtualOrderDeliveryDto.setTotalAmount(BigDecimal.valueOf(21L));
        virtualOrderDeliveryDto.setDayDesc("今天");
        virtualOrderDeliveryDto.setDeliveryActualPrice(BigDecimal.valueOf(2L));
        virtualOrderDeliveryDto.setDeliveryOriginalPrice(BigDecimal.valueOf(4L));
        virtualOrderDeliveryDto.setTotalGoodsCount(1);
        virtualOrderDeliveryDto.setTotalGoodsActualAmount(BigDecimal.valueOf(18L));
        virtualOrderDeliveryDto.setTotalGoodsOriginalAmount(BigDecimal.valueOf(24L));
        virtualOrderDeliveryDto.setTotalPackageActualAmount(BigDecimal.valueOf(1L));
        virtualOrderDeliveryDto.setTotalPackageOriginalAmount(BigDecimal.valueOf(3L));
        virtualOrderDto.setDeliveries(Lists.newArrayList(virtualOrderDeliveryDto));

        VirtualOrderDeliveryGoodsDto virtualOrderDeliveryGoodsDto=new VirtualOrderDeliveryGoodsDto();
        virtualOrderDeliveryGoodsDto.setGoodsCode("5615118281");
        virtualOrderDeliveryGoodsDto.setGoodsName("农家小炒肉饭");
        virtualOrderDeliveryGoodsDto.setGoodsCount(1);
        virtualOrderDeliveryGoodsDto.setGoodsImgUrl("");
        virtualOrderDeliveryGoodsDto.setGoodsOriginalPrice(BigDecimal.valueOf(24L));
        virtualOrderDeliveryGoodsDto.setGoodsActualPrice(BigDecimal.valueOf(18L));
        virtualOrderDeliveryGoodsDto.setPackageOriginalPrice(BigDecimal.valueOf(3L));
        virtualOrderDeliveryGoodsDto.setPackageActualPrice(BigDecimal.valueOf(1L));
        virtualOrderDeliveryDto.setDeliveryGoods(Lists.newArrayList(virtualOrderDeliveryGoodsDto));

        System.out.println(JSON.toJSONString(virtualOrderDto));
    }

    @Test
    public void genOrderPaymentParaJson(){
        OrderPaymentParam orderPaymentParam=new OrderPaymentParam();
        orderPaymentParam.setOrderId(1521682721538048L);
        orderPaymentParam.setDeliveryBuildingId(1448557259001830L);
        orderPaymentParam.setDeliveryAddressId(1488576817463296L);
        orderPaymentParam.setDeliveryCompany("米立");
        orderPaymentParam.setDeliveryFloor("11");
        orderPaymentParam.setRealName("盖伦");
        orderPaymentParam.setMobileNo("15316388732");
        orderPaymentParam.setRedPacketId(1521682722538041L);
        orderPaymentParam.setNotifyType((short)1);
        orderPaymentParam.setFormId("lkjhgfdsa");
        orderPaymentParam.setSex((short)1);

        DeliveryTimezoneParam deliveryTimezoneParam=new DeliveryTimezoneParam();
        deliveryTimezoneParam.setDeliveryId(1448557259001830L);
        deliveryTimezoneParam.setId(1448557259001830L);
        deliveryTimezoneParam.setShopCode("31010001");

        orderPaymentParam.setDeliveryTimezones(Lists.newArrayList(deliveryTimezoneParam));
        System.out.println(JSON.toJSONString(orderPaymentParam));

    }

    @Test
    public void genWechatMiniPayJson(){
        WechatMiniPayDto wechatMiniPayDto=new WechatMiniPayDto();
        wechatMiniPayDto.setTimeStamp(System.currentTimeMillis()+"");
        wechatMiniPayDto.setNonceStr("asdf");
        wechatMiniPayDto.setSignType("MD5");
        wechatMiniPayDto.setPaySign("asdfasdf");
        wechatMiniPayDto.setDataPackage("prepay_id=123456");

        System.out.println(JSON.toJSONString(wechatMiniPayDto));
    }

    @Test
    public void genOrderDtoJson(){
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1521682721538048L);
        orderDto.setTotalGoodsOriginalAmount(BigDecimal.valueOf(24));
        orderDto.setTotalGoodsActualAmount(BigDecimal.valueOf(18));
        orderDto.setTotalDeliveryOriginalAmount(BigDecimal.valueOf(4));
        orderDto.setTotalDeliveryActualAmount(BigDecimal.valueOf(2));
        orderDto.setTotalPackageOriginalAmount(BigDecimal.valueOf(3));
        orderDto.setTotalPackageActualAmount(BigDecimal.valueOf(1));
        orderDto.setTotalGoodsCount(1);
        orderDto.setTotalPayAmount(BigDecimal.valueOf(9L));
        orderDto.setTotalOrderDays(1);
        orderDto.setDeliveryStartTime("11:00");
        orderDto.setDeliveryEndTime("12:00");
        orderDto.setOrderDate(new Date());
        orderDto.setRedPacketId(1521582721538048L);
        orderDto.setRedPacketAmount(BigDecimal.valueOf(12));


        OrderDeliveryDto orderDeliveryDto=new OrderDeliveryDto();
        orderDeliveryDto.setDeliveryDate("2018-10-08");
        orderDeliveryDto.setTotalAmount(BigDecimal.valueOf(21L));
        orderDeliveryDto.setDayDesc("今天");
        orderDeliveryDto.setDeliveryActualPrice(BigDecimal.valueOf(2L));
        orderDeliveryDto.setDeliveryOriginalPrice(BigDecimal.valueOf(4L));
        orderDeliveryDto.setTotalGoodsCount(1);
        orderDeliveryDto.setTotalGoodsActualAmount(BigDecimal.valueOf(18L));
        orderDeliveryDto.setTotalGoodsOriginalAmount(BigDecimal.valueOf(24L));
        orderDeliveryDto.setTotalPackageActualAmount(BigDecimal.valueOf(1L));
        orderDeliveryDto.setTotalPackageOriginalAmount(BigDecimal.valueOf(3L));
        orderDto.setDeliveries(Lists.newArrayList(orderDeliveryDto));

        OrderDeliveryGoodsDto orderDeliveryGoodsDto=new OrderDeliveryGoodsDto();
        orderDeliveryGoodsDto.setGoodsCode("5615118281");
        orderDeliveryGoodsDto.setGoodsName("农家小炒肉饭");
        orderDeliveryGoodsDto.setGoodsCount(1);
        orderDeliveryGoodsDto.setGoodsImgUrl("");
        orderDeliveryGoodsDto.setGoodsOriginalPrice(BigDecimal.valueOf(24L));
        orderDeliveryGoodsDto.setGoodsActualPrice(BigDecimal.valueOf(18L));
        orderDeliveryGoodsDto.setPackageOriginalPrice(BigDecimal.valueOf(3L));
        orderDeliveryGoodsDto.setPackageActualPrice(BigDecimal.valueOf(1L));
        orderDeliveryDto.setDeliveryGoods(Lists.newArrayList(orderDeliveryGoodsDto));

        System.out.println(JSON.toJSONString(orderDto));
    }

    @Test
    public void genRefundOrderParam(){
        RefundOrderParam refundOrderParam=new RefundOrderParam();
        refundOrderParam.setOrderId(1521582721538048L);
        refundOrderParam.setCancelReason("取消原因");
        refundOrderParam.setDeliveryIds(Sets.newHashSet(1521582721538048L));

        System.out.println(JSON.toJSONString(refundOrderParam));
    }

    @Test
    public void genMultiDeliveryTimezone(){
        List<TimezoneDto> deliveryTimezones=new ArrayList<>();

        TimezoneDto deliveryTime=new TimezoneDto();
        deliveryTime.setId(1L);
        deliveryTime.setCutoffTime("11:00");
        deliveryTime.setStartTime("11:15");
        deliveryTime.setEndTime("12:00");
        deliveryTime.setMaxCapacity(120);
        deliveryTime.setIsDefault(true);
        deliveryTimezones.add(deliveryTime);

        deliveryTime=new TimezoneDto();
        deliveryTime.setId(2L);
        deliveryTime.setCutoffTime("11:30");
        deliveryTime.setStartTime("11:45");
        deliveryTime.setEndTime("12:30");
        deliveryTime.setMaxCapacity(120);
        deliveryTime.setIsDefault(false);
        deliveryTimezones.add(deliveryTime);

        deliveryTime=new TimezoneDto();
        deliveryTime.setId(3L);
        deliveryTime.setCutoffTime("12:00");
        deliveryTime.setStartTime("12:15");
        deliveryTime.setEndTime("13:00");
        deliveryTime.setMaxCapacity(120);
        deliveryTime.setIsDefault(false);
        deliveryTimezones.add(deliveryTime);


        System.out.println(JSON.toJSONString(deliveryTimezones));

    }


}