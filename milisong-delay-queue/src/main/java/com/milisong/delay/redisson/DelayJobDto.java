package com.milisong.delay.redisson;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * 数据传输对象
 * @author User
 *
 */
@Getter
@Setter
public class DelayJobDto {
	
	//业务参数
	private Map<String,Object> params;
	
	//业务执行实现类
	private  Class<?> aClass;
	

}
