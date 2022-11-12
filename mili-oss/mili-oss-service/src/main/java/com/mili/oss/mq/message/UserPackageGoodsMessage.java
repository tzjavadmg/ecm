package com.mili.oss.mq.message;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @description: 打包装袋商品信息
 * @program: mili-ofc-wos
 * @author: codyzeng@163.com
 * @date: 2019/5/15 20:20
 */
@Getter
@Setter
public class UserPackageGoodsMessage {
    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品数量
     */
    private Integer goodsCount;

    /**
     * 单品明细
     */
    private List<UserPackageGoodsMessage> items;
}
