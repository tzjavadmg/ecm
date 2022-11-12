package com.milisong.pms.prom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户红包弹层配置
 *
 * @author sailor wang
 * @date 2018/11/6 6:33 PM
 * @description
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToastRedPacketConfig {

    //主标题图片
    private String bgImg;

    // 弹层subtitle
    private String subTitle;

    // 展示红包个数
    private Integer showNum;

    // 每天弹层几次
    private Integer toastPerDay;

    // 弹层开关
    private Boolean toastSwitch;


}