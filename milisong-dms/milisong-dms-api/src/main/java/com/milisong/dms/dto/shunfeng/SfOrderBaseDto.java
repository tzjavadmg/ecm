package com.milisong.dms.dto.shunfeng;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author zhaozhonghui
 * @date 2018-10-23
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "sf")
public class SfOrderBaseDto {
    /** 开发者ID */
    private String devId;
    /** 秘钥 */
    private String key;
    /** 版本 */
    private int version;
    /** 创建订单接口 */
    private String createOrderUrl;
    /** 配送员h5轨迹路线 */
    private String riderH5View;
    /** 通知客服电话 */
    private String notifyMobile;
    /** 通知客服邮箱 */
    private String notifyMail;
    /** 测试标识 */
    private Boolean testFlag;
    /** 未接单超时时间 分钟 */
    private Integer overTime;
    /** 午餐重量 */
    private Integer lunchWeight;
    /** 早餐重量 */
    private Integer bfWeight;
}
