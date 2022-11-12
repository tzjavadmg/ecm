package com.milisong.pms.prom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 满减
 *
 * @author sailor wang
 * @date 2018/11/8 12:42 PM
 * @description
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullReducePair  {

    private BigDecimal full;//满多少

    private BigDecimal reduce;//减多少

    private Integer level;//档位

}