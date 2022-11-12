package com.milisong.breakfast.scm.common.param;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 	<if test="startRow != null">
   		limit #{startRow}, #{pageSize}
   	</if>
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class PageInfo {
	 private Integer pageSize;
	 private Integer startRow;
}
