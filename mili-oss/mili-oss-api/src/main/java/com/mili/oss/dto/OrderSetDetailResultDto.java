package com.mili.oss.dto;

import lombok.Data;

import java.io.Serializable;

/**
*@author    created by benny
*@date  2018年10月30日---下午12:05:25
*
*/
@Data
public class OrderSetDetailResultDto extends OrderSetDetailDto implements Serializable {

    private Integer customerSum;
}
