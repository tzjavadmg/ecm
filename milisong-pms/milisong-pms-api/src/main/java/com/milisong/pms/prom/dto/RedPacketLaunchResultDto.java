package com.milisong.pms.prom.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @author sailor wang
 * @date 2018/9/22 下午6:15
 * @description
 */
@Data
public class RedPacketLaunchResultDto implements Serializable {

    private static final long serialVersionUID = -2522179395807192044L;

    private Set<Long> validOrderSet;
}