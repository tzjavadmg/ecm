package com.milisong.pms.prom.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 砍红包业务相关的注解
 * @author sailor wang
 * @date 2018/8/31 下午5:40
 * @description
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AfterCutRedPacket {
	String value() default "";
}
