package com.milisong.pms.prom.dto.groupbuy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

/**
 * @author sailor wang
 * @date 2019/5/21 8:54 PM
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityGroupBuyDto implements Serializable {
    private Long id;

    private String name;

    //剩余库存
    private Integer leftStock;

    //商品名称
    private String goodsName;

    //商品code
    private String goodsCode;

    //商品图片
    private String goodsPic;

    //商品描述
    private String goodsDesc;

    private BigDecimal goodsPrice;

    private BigDecimal buyPrice;

    private Integer minJoinNum;

    private Date startDate;

    private Date endDate;

    private Integer validPeriod;

    private Date deliveryDate;

    private String bannerImg;

    private String bgImg;

    private String ruleImg;

    private String shareTitle;

    private String shareImg;

    //几月几号送达：xx-xx送达
    private String dateDeliveryText;

    //周几送达：周三 xx-xx 送达
    private String weekDeliveryText;

    //剩余多少时间，单位：秒
    private Integer expireTime;

    public Integer getExpireTime() {
        if (endDate != null) {
            int seconds = Seconds.secondsBetween(DateTime.now(), new DateTime(endDate)).getSeconds();
            return seconds > 0 ? seconds : 0;
        }
        return 0;
    }

    public String getDateDeliveryText() {
        if (deliveryDate!=null){
            DateTime time = new DateTime(deliveryDate);

            int month = time.getMonthOfYear();
            int day = time.getDayOfMonth();
            String monthStr = String.valueOf(month);
            if (month < 10){
                monthStr = "0"+month;
            }
            String dayStr = String.valueOf(day);
            if (day < 10){
                dayStr = "0"+day;
            }
            return monthStr + "-" + dayStr;
        }
        return dateDeliveryText;
    }

    public String getWeekDeliveryText() {
        if (deliveryDate!=null){
            //当前时间
            DateTime.Property pDow = new DateTime(deliveryDate).dayOfWeek();
            //星期x
            String weekText = pDow.getAsText(Locale.CHINA);
            return "周"+weekText.substring(2);
        }
        return weekDeliveryText;
    }

}