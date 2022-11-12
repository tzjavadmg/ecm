package com.milisong.pms.prom.dto.invite;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityUserInviteDto implements Serializable {

    private static final long serialVersionUID = -6622374929788197927L;
    private Long id;

    private String name;

    private Long originateUserId;

    private Byte type;

    private String goodsJson;

    private Integer validDays;

    private Integer maxCount;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    private Date createTime;

    private Date updateTime;

}