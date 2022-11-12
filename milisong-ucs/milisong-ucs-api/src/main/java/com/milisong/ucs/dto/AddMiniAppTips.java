package com.milisong.ucs.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sailor wang
 * @date 2018/11/8 2:37 PM
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddMiniAppTips {

    private String imgUrl;

    private Integer showTimes;

}