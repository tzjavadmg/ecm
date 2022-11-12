package com.milisong.pms.prom.dto;

import java.io.Serializable;

/**
 * @author sailor wang
 * @date 2018/9/13 下午4:19
 * @description
 */
public class LCUserRedPacketResponse extends UserRedPacketResponse implements Serializable {

    private static final long serialVersionUID = 9191116348904802912L;

    private Boolean receivedCoupon;//是否领取过早餐券

    public Boolean getReceivedCoupon() {
        return receivedCoupon;
    }

    public void setReceivedCoupon(Boolean receivedCoupon) {
        this.receivedCoupon = receivedCoupon;
    }
}