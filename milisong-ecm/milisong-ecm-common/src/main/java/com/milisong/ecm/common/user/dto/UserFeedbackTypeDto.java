package com.milisong.ecm.common.user.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sailor wang
 * @date 2018/9/5 下午4:03
 * @description
 */
@Data
public class UserFeedbackTypeDto implements Serializable {
    private static final long serialVersionUID = -3464073662395534073L;

    private Long id;

    private String name;

    private Integer sort;

}