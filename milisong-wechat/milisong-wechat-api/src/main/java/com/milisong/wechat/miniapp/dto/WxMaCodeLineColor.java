package com.milisong.wechat.miniapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用于描述二维码（小程序码）颜色
 * 使用 rgb 设置颜色 例如 {"r":"xxx","g":"xxx","b":"xxx"} 十进制表示，默认全 0
 *
 * @author sailor wang
 * @date 2018/10/24 下午4:32
 * @description
 */
@Data
@AllArgsConstructor
public class WxMaCodeLineColor {
    private String r = "0", g = "0", b = "0";
}
