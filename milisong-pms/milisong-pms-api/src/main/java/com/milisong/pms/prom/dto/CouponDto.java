package com.milisong.pms.prom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/1/11   17:04
 *    desc    : 券dto
 *    version : v1.0
 * </pre>
 */
@Data
public class CouponDto implements Serializable {
    private static final long serialVersionUID = -1873644713214046265L;
    /**
     * ID
     */
    private Long id;

    /**
     * 券名称
     */
    private String name;

    /**
     * 券类型：1 折扣券 2 商品券
     */
    private Byte type;

    /**
     *  针对type = 2，0 商品券 1商品套餐券
     */
    private Byte label;

    /**
     * 门槛天数(满x天)
     */
    private Integer limitDays;

    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 折扣/优惠金额
     */
    private BigDecimal discount;

    /**
     * 是否与其它活动共享，0 不同享 1 同享
     */
    private Byte isShare;

    /**
     * 有效天数
     */
    private Integer validDays;

    /**
     * 券使用规则
     */
    private String rule;

    /**
     * 券数量
     */
    private Integer quantity;

    /**
     * 业务线：0 早餐、1  午餐、2 下午茶、3 晚餐、4 夜宵
     */
    private Byte businessLine;

    /**
     * 发布状态：0 下线、1 上线
     */
    private Byte status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
