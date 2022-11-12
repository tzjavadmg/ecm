package com.mili.oss.mq.message;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @description: 配送包裹(配送联)
 * @program: mili-ofc-wos
 * @author: codyzeng@163.com
 * @date: 2019/5/15 19:34
 */
@Getter
@Setter
public class DeliveryPackageMessage {


//    private transient Long companyId;
    /**
     * 配送联编号
     */
    private String deliveryNo;

    /**
     * 配送日期
     */
    private String deliveryDate;

    /**
     * 取餐开始时间
     */
    private String takeMealStartTime;

    /**
     * 取餐结束时间
     */
    private String takeMealEndTime;

    /**
     * 取餐点
     */
    private String takeMealAddr;

    /**
     * 打包数量
     */
    private Integer userPackageCount;

    private List<UserPackageMessage> userPackageList;
}
