package com.milisong.ecm.lunch.goods.dto;

import com.milisong.ecm.common.dto.BannerDto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/16 15:37
 */
@Getter
@Setter
public class ShopConfigDto {

    /**
     * 门店banner图地址 //FIXME 存对象，存图片地址，链接地址
     */
    private String bannerImageUrl;

    /**
     * 配送费
     */
    private BigDecimal deliveryOriginalAmount;
    /**
     * 配送费优惠价
     */
    private BigDecimal deliveryDiscountAmount;
    /**
     * 打包费
     */
    private BigDecimal packageOriginalAmount;
    /**
     * 打包费优惠价
     */
    private BigDecimal packageDiscountAmount;

    /**
     * 未支付订单过期自动取消时间
     */
    private Integer unPayExpiredTime;

    /**
     * 用户收货地址重名提示弹窗次数限制
     */
    private Integer sameCompanyHintCount;

    /**
     * 截单配送时间
     */
    private List<DeliveryTimezone> deliveryTimezones;

    /**
     * 小程序分享图片地址
     */
    private String sharePictureUrl;

    /**
     * 小程序分享标题
     */
    private String shareTitle;

    /**
     * banner图及链接
     */
    private List<BannerDto> bannerList;

    /**
     * 餐品海报图地址
     */
    private String goodsPosterUrl;

    /**
     * 提示用收藏小程序提示配置
     */
    private AddMiniAppTips addMiniAppTips;
    
    /**
     * 订单完成时间(我的午餐计划是否展示已完成订单)
     */
    private Integer orderEndTime;

    @Getter
    @Setter
    static public class AddMiniAppTips{
        private String imgUrl;

        private Integer showTimes;
    }

    @Getter
    @Setter
    static public class DeliveryTimezone {

        private Long id;
        /**
         * 是否默认值
         */
        private Boolean isDefault;
        /**
         * 门店最大产量
         */
        private Integer maxCapacity;
        /**
         * 截单时间字符串 11:00-12:00
         */
        private String cutoffTime;
        /**
         * 配送开始时间字符串 HH:mm
         */
        private String startTime;

        /**
         * 配送结束时间字符串 HH:mm
         */
        private String endTime;
    }
}
