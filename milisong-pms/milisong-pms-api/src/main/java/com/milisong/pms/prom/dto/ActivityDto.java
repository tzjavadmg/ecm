package com.milisong.pms.prom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sailor wang
 * @date 2018/9/13 下午2:30
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDto implements Serializable {
    private static final long serialVersionUID = -972864872956581880L;

    private Long id;

    private String name;

    private String descript;

    private Date startDate;

    private Date endDate;

    private Byte type;

    private Integer userLimit;

    private Integer activityLimit;

    private String dayOfWeek;

    private Byte isAllGoods;

    private String goodsCatalogIds;

    private String goodsIds;

    private String cityIds;

    private String buildingIds;

    private String companyIds;

    private BigDecimal minAmount;

    private Byte status;

    private Byte isDelete;

    private Byte isShare;

    public static void main(String[] args) {

    }
}