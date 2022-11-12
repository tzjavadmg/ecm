package com.milisong.breakfast.scm.orderset.dto.result;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 门店集单查询结果对象
 * @author yangzhilong
 *
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSetSearchResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5622190162893987784L;
	// 楼宇集单号
	//private String setNo;
	//楼宇集单号描述
    //private String setNoDescription;
    // 子集单的id
    private Long detailSetId;
    // 子集单号
    private String detailSetNo;
    // 子集单号描述
    private String detailSetNoDescription;
    // 门店id
    private Long shopId;
    // 楼宇id
    private Long buildingId;
    // 楼宇简称
    private String buildingAbbreviation;
    // 楼层
    private String deliveryFloor;
    // 公司名称
    private String companyAbbreviation;
    // 是否打印
    private Boolean isPrint;
    // 商品份数
    private Integer goodsSum;
    
    // 订单信息
    private List<OrderSetSearchResultOrder> orderList;
    
    // 详细配送地址
    private String detailDeliveryAddress;
    // 配送日期
    private Date detailDeliveryDate;
    // 集单状态
    private Byte status;
}
