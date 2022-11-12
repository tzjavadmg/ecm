package com.milisong.oms.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/23 19:06
 */
@Getter
@Setter
public class DeliveryTimezoneParam {

    /**
     * 配送ID
     */
    private Long deliveryId;
    /**
     * 配送日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date deliveryDate;
    /**
     * 门店编码
     */
    private String shopCode;
    /**
     * 所属业务线
     */
    private Byte businessLine;
    /**
     * 配送时区ID
     */
    private Long id;

    /**
     * 午餐在用，后面统一用id
     */
    @Deprecated
    private Long deliveryTimezoneId;

    /**
     * 是否默认值
     */
    private Boolean isDefault;
    /**
     * 门店最大产量
     */
    private Integer maxCapacity;
    /**
     * 截单时间字符串 11:00-12:00
     */
    private String cutoffTime;
    /**
     * 配送开始时间字符串 HH:mm
     */
    private String startTime;
    /**
     * 配送结束时间字符串 HH:mm
     */
    private String endTime;
    /**
     * 是否可用
     */
    private boolean available;
}
