package com.milisong.scm.base.dto.mq;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 门店创建事件
 * @author yangzhilong
 *
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopCreateMqDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3313194942850067183L;

	private Long id;

    private String code;

    private String name;
    // 配置的来源
    private Long srcId;
}
