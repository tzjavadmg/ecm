package com.milisong.ecm.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/22 10:41
 */
@AllArgsConstructor
@Getter
public enum ConfigItemEnum {
    /**
     * 截单时间
     */
    CUT_OFF_ORDER_DELIVERY_TIME("cutOffOrderDeliveryTime"),
    /**
     * 配送时段配置
     */
    DELIVERY_TIMEZONE("deliveryTimezone"),
    /**
     * 配送费
     */
    DELIVERY_ORIGINAL_AMOUNT("deliveryOriginalAmount"),
    /**
     * 配送费优惠价
     */
    DELIVERY_DISCOUNT_AMOUNT("deliveryDiscountAmount"),
    /**
     * 打包费
     */
    PACKAGE_ORIGINAL_AMOUNT("packageOriginalAmount"),
    /**
     * 打包费优惠价
     */
    PACKAGE_DISCOUNT_AMOUNT("packageDiscountAmount"),
    /**
     * 用户收费地址中同名公司提示次数
     */
    SAME_COMPANY_HINT_COUNT("sameCompanyHintCount"),
    /**
     * 未支付订单，过期取消时间，单位：分钟
     */
    UN_PAY_EXPIRED_TIME("unPayExpiredTime"),
    /**
     * 小程序分享图片地址
     */
    SHARE_PICTURE_URL("sharePictureUrl"),

    /**
     * 小程序分享标题
     */
    SHARE_TITLE("shareTitle"),

    /**
     * 小程序banner图及链接
     */
    BANNER("banner"),

    /**
     * LSB搜索范围
     */
    LBS_DISTANCE("lbsDistance"),

    /**
     * 餐品海报图地址
     */
    GOODS_POSTER_URL("goodsPosterUrl"),

    /**
     * 提示用收藏小程序提示配置
     */
    ADD_MINI_APP_TIPS("addMiniAppTips"),

    /**
     * 订单完成时间(超过该时间我的午餐计划不显示订单)
     */
    ORDER_END_TIME("orderEndTime"),

    /**
     * 档期标识
     */
    MOVIE_FLAG("movieFlag"),

    /**
     * 库存标识
     */
    STOCK_FLAG("stockFlag"),

    /**
     * 限购数量
     */
    PURCHASE_LIMIT("purchaseLimit"),


    /**
     * 分享给好友banner配置
     */
    SHARE_FRIEND_BANNER("shareFriendBanner"),
    ;

    private String value;


}
