package com.milisong.ecm.common.user.dto;

import java.io.Serializable;

/**
 * @author sailor wang
 * @date 2018/12/3 8:21 PM
 * @description
 */
public class FeedbackTypeDto extends BaseDto implements Serializable {
    private static final long serialVersionUID = -1928256464289549783L;

    private Boolean transit;

    public Boolean getTransit() {
        return transit;
    }

    public void setTransit(Boolean transit) {
        this.transit = transit;
    }
}