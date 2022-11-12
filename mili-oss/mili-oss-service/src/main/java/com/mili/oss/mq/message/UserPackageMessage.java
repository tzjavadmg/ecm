package com.mili.oss.mq.message;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @description: 打包装袋(顾客)联
 * @program: mili-ofc-wos
 * @author: codyzeng@163.com
 * @date: 2019/5/15 15:41
 */
@Getter
@Setter
public class UserPackageMessage {

    /**
     * 顾客联编号
     */
    private String receiptNo;

    /**
     * 顾客姓名
     */
    private String userName;

    /**
     * 用户手机号
     */
    private String mobileNo;

    /**
     * 序号
     */
    private Integer index;

    /**
     * 单品
     */
    private List<UserPackageGoodsMessage> itemGoodsList;

    /**
     * 组合商品（目前早餐一个袋子套餐最多放一个，但不排除以后放多个可能性）
     */
    private List<UserPackageGoodsMessage> comboGoodsList;
}
