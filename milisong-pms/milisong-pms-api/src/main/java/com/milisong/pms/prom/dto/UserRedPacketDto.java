package com.milisong.pms.prom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRedPacketDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    //红包类型：1 分享红包、2 新人红包、3 砍红包
    private Byte type;

    private Long acitivtyId;

    private Long userActivityId;

    private Long userId;

    private Long orderId;

    private BigDecimal amount;

    private Date expireTime;

    private String expireTimeText;//到期时间戳 如：2018-07-30

    private String leftTimeText;//还剩几日 如：剩2日

    private Byte isShare;//是否同享

    private Byte isValid;//是否有效

    private Byte isUsed;//是否用过

    private Boolean selected;//默认选中

    private String mark;//备注

    private String bgImage;//弹层背景图

    public String getExpireTimeText() {
        if (expireTime != null){
            return new DateTime(expireTime).toString("yyyy-MM-dd");
        }
        return expireTimeText;
    }

    public void setExpireTimeText(String expireTimeText) {
        this.expireTimeText = expireTimeText;
    }

    public String getLeftTimeText() {
        if (expireTime != null){
            DateTime now = DateTime.now();
            DateTime exTime = new DateTime(expireTime);
            int days = Days.daysBetween(now,exTime).getDays();
            if (days > 0){
                return "剩"+days+"日";
            }else if (days == 0){
                return "今日到期";
            }
        }
        return leftTimeText;
    }

    public String getMark() {
        if (isShare != null){
            if (isShare.intValue() == 1){
                //共享
                return "仅限米立送小程序使用";
            }else {
                //不共享
                return "不可与其他活动同享";
            }
        }
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public void setLeftTimeText(String leftTimeText) {
        this.leftTimeText = leftTimeText;
    }
}