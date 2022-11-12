package com.milisong.pms.prom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sailor wang
 * @date 2018/9/13 下午8:30
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityUserRedPacketDto extends BaseDto implements Serializable {
    private static final long serialVersionUID = -8517380440607995266L;

    private Long id;

    private Long activityId;

    private Long userId;

    private String openId;

    private String nickName;

    private String headPortraitUrl;

    private Byte clickCount;

    private Byte isSuccess;

    private BigDecimal amount;

    private Byte players;

    private Byte valid;

    private Byte isShare;

    private Byte clickLimit;

    private Date startDate;

    private Date endDate;

    private Date createTime;

    private String createTimeText;

    public String getCreateTimeText() {
        if (createTime != null){
            return new DateTime(createTime).toString("MM.dd HH:mm:ss");
        }
        return createTimeText;
    }

    public void setCreateTimeText(String createTimeText) {
        this.createTimeText = createTimeText;
    }
}