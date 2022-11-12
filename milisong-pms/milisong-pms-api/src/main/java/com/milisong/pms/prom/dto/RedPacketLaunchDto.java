package com.milisong.pms.prom.dto;

import com.milisong.pms.prom.enums.RedPacketLaunchType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sailor wang
 * @date 2018/9/22 下午7:07
 * @description
 */
@Data
public class RedPacketLaunchDto implements Serializable {

    private static final long serialVersionUID = -5341955103466450198L;

    private Long launchId;

    private RedPacketLaunchType launchType;

    private Date launchDate;
}