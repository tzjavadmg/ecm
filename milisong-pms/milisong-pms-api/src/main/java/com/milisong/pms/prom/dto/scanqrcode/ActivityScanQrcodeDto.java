package com.milisong.pms.prom.dto.scanqrcode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 早餐扫码活动
 *
 * @author sailor wang
 * @date 2019/5/9 5:39 PM
 * @description
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityScanQrcodeDto implements Serializable {
    private Long id;

    private String name;

    private String qrcode;

    private Byte type;

    private Byte giftType;

    private String giftIds;

    private String bgImg;

    private Date startDate;

    private Date endDate;

    /**
     * 活动是否激活
     */
    private Boolean isActive;

    private String remark;

    public Boolean getIsActive() {
        Date now = new Date();
        if (startDate != null){
            if (startDate.after(now)){
                remark = "活动还未开始哦~";
                return false;
            }
        }
        if (endDate != null){
            if (now.after(endDate)){
                remark = "活动已经结束了哦~";
                return false;
            }
        }
        return Boolean.TRUE;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    private static final long serialVersionUID = 1L;

}