package com.milisong.upms.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 原始的数据权限dto
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class UserDataPermissionDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String type;

    private String dataValue;

    private String dataDesc;
}
