package com.milisong.pms.prom.annotation;

import com.milisong.pms.prom.enums.MQRecordType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/1/10   11:13
 *    desc    : 标识记录MQ信息日志
 *    version : v1.0
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RecordMQ {

    /**
     * 标识记录类型
     * @see MQRecordType
     * @return
     */
    MQRecordType value() default MQRecordType.POINT;
}
