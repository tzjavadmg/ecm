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

import static com.milisong.pms.prom.enums.GrouyBuyEnum.GROUP_BUY_STATUS.CREATE_PAY;
import static com.milisong.pms.prom.enums.GrouyBuyEnum.GROUP_BUY_STATUS.TIMEOUT;

/**
 * 用户拼团
 * @author sailor wang
 * @date 2019/5/17 8:32 PM
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class  UserGroupBuyDto implements Serializable {
    private static final long serialVersionUID = 4235023109377659311L;
    private Long id;

    private Long activityGroupBuyId;

    private String name;

    //剩余库存
    private Integer leftStock;

    //商品名称
    private String goodsName;

    //商品code
    private String goodsCode;

    //商品图片
    private String goodsPic;

    //商品价格
    private BigDecimal goodsPrice;

    //商品描述
    private String goodsDesc;

    private BigDecimal buyPrice;

    private Integer minJoinNum;

    private Date startDate;

    private Date endDate;

    private Integer validPeriod;

    private Date deliveryDate;

    //拼团实例状态 @see GROUP_BUY_STATUS
    private Byte status;

    //订单id
    private Long orderId;

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

    //活动是否结束
    private Boolean isOver;

    //公司id
    private Long companyId;

    //结束时间
    private String endDateText;

    public Byte getStatus() {
        if (CREATE_PAY.getCode().equals(status)){//拼团中
            Date endDate = getEndDate();
            if (endDate != null){
                if (Seconds.secondsBetween(DateTime.now(),new DateTime(endDate)).getSeconds() <= 1){
                    return TIMEOUT.getCode();
                }
            }
        }
        return status;
    }

    public static void main(String[] args) {
        DateTime now = DateTime.now();
        DateTime end = now.plusSeconds(2);
        System.out.println(Seconds.secondsBetween(now,end).getSeconds());
    }

    public String getEndDateText() {
        if (getEndDate() != null){
            return new DateTime(getEndDate()).toString("yyyy-MM-dd HH:mm:ss");
        }
        return endDateText;
    }

    public Boolean getIsOver() {
        if (getStartDate() != null && getEndDate() != null){
            Date now = new Date();
            Boolean isRunning = now.after(getStartDate()) && now.before(getEndDate());
            return !isRunning;
        }
        return isOver;
    }

    public Integer getExpireTime() {
        if (endDate != null) {
            int seconds = Seconds.secondsBetween(DateTime.now(), new DateTime(endDate)).getSeconds();
            return seconds > 0 ? seconds : 0;
        }
        return 0;
    }

    public String getDateDeliveryText() {
        if (deliveryDate != null){
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