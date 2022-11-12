package com.milisong.breakfast.scm.order.dto.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.milisong.breakfast.scm.common.annotation.Export;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhaozhonghui
 * @date 2018-11-20
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OrderExportResult implements Serializable {
    private static final long serialVersionUID = 5616704675820724223L;
    /**
     * 子集单id
     */
    private Long id;

    @Export(value = "配送单", rank = 0)
    private String description;
    @Export(value = "顾客联单号", rank = 1)
    private String coupletNo;
    /**
     * 门店id
     */
    private Long shopId;

    @Export(value = "门店", rank = 2)
    private String shopName;

    @Export(value = "期望到达时间", rank = 10)
    private String expectArriveTime;

    @Export(value = "实际到达时间", rank = 20)
    private String arriveTime;

    @Export(value = "下单日期", rank = 30)
    private String orderDate;

    @Export(value = "客户信息", rank = 40)
    private String userInfo;

    @Export(value = "配送地址", rank = 50)
    private String deliveryAddress;
    @Export(value = "取餐点", rank = 51)
    private String mealAddress;

    private List<OrderGoodsResult> goodsResultList;
    @Export(value = "商品", rank = 60)
    private String goodsInfo;
    //    @Export(value = "商品总数",rank = 61)
    private int goodsTotalNumber;

    @Export(value = "支付总金额", rank = 70)
    private BigDecimal actualPayAmount;

    private Byte deliveryStatus;

    @Export(value = "配送状态", rank = 80)
    private String deliveryStatusStr;

    private String orderNo;

}
