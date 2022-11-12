package com.milisong.breakfast.scm.orderset.dto.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 集单列表返回值dto
 * 
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class OrderSetDetailResultDto extends OrderSetDetailDto{

    /**
	 * 
	 */
	private static final long serialVersionUID = 5558280476197849531L;
	
	@ApiModelProperty("顾客联数量")
	private Integer customerSum;
}
