package com.milisong.ucs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 登录后领取新人红包
 *
 * @author sailor wang
 * @date 2018/8/31 下午5:40
 * @description
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReceiveNewRedPacketAfterLogin {
    String value() default "";
}
