package com.milisong.pms.prom.dto;

import com.farmland.core.api.PageParam;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/1/13   10:36
 *    desc    : 券条件信息dto
 *    version : v1.0
 * </pre>
 */
@Data
public class CouponQueryDto extends PageParam implements Serializable {
    private static final long serialVersionUID = -1401763062315362621L;

    /**
     * 券名称
     */
    private String name;

    /**
     * 发布状态：0 下线、1 上线
     */
    private Byte status;

    /**
     * 券类型：1 折扣券 2 商品券
     */
    private Byte type;

    /**
     *  针对type = 2，0 商品券 1商品套餐券
     */
    private Byte label;

    /**
     * 业务线：0 早餐、1  午餐、2 下午茶、3 晚餐、4 夜宵
     */
    private Byte businessLine;

}
