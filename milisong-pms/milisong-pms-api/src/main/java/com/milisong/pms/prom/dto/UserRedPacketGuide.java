package com.milisong.pms.prom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author sailor wang
 * @date 2018/11/8 12:25 PM
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRedPacketGuide implements Serializable {
    private static final long serialVersionUID = -4828553547287825027L;
    //主标题图片
    private String bgImg;

    //副标题
    private String subTitle;

    private List<UserRedPacketDto> redPacketList;

}