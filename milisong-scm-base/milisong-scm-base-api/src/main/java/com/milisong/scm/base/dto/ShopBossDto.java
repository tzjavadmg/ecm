package com.milisong.scm.base.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 给BOSS配数据权限的门店dto类
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class ShopBossDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

    private String code;

    private String name;

    private Byte status;
}
