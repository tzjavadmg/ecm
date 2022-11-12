package com.milisong.pms.prom.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 红包发起入口
 *
 * @author sailor wang
 * @date 2018/9/22 下午6:08
 * @description
 */
@Data
public class RedPacketLaunchQueryDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = -9087803544257491349L;

    private List<RedPacketLaunchDto> redPacketLaunchList;

    private Byte businessLine;
}