package com.milisong.scm.goods.util;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.milisong.scm.goods.constant.GoodsStatusEnum;
import com.milisong.scm.goods.domain.Goods;

/**
 * 商品工具类
 * @author youxia 2018年9月4日
 */
public class GoodsUtil {
	
	/**
	 * 生成商品编码
	 * @return
	 * youxia 2018年9月4日
	 */
    public static String generateGoodsCode(){
        StringBuilder sb = new StringBuilder();
        int first = (int)(1+(Math.random()*9));
        sb.append(first);
        for(int i = 0; i < 9; i++) {
            sb.append((int)(Math.random()*9));
        }
        return sb.toString();
    }
    
    /**
     * 构建商品详细状态
     * @param date 日期
     * @param list 商品
     * youxia 2018年9月4日
     */
    public static void buildStatus(Date date, List<Goods> list) {
    	if (CollectionUtils.isNotEmpty(list) && date != null) {
//    		list.forEach(goods -> {
    		for (Goods goods : list) {
				
//			}
    			String status = "S" + goods.getStatus();
    			if (GoodsStatusEnum.STATUS_CLOSE.getValue().equals(status)) {
    				// 已停用状态的,子状态为已下线
    				goods.setDetailStatus(Byte.parseByte(GoodsStatusEnum.DETAIL_STATUS_DOWNLINE.getValue().replaceAll("DS", "")));
    			} else {
    				// 如果开始时间为今天之后：子状态为即将上线；如果为当前日期或今天之前：子状态根据结束日期来定（使用中、今日下线、明日下线）
    				if (DateUtils.isSameDay(date, goods.getBeginDate()) || goods.getBeginDate().getTime() < date.getTime()) {
        				// 子状态根据结束日期定(是否不限定结束时间，0否1是，0：限定，1：不限定)
//    					if (goods.getIsLimitedTime() == 1) {
//							// 不限定结束时间
//    						goods.setDetailStatus(Byte.parseByte(GoodsStatusEnum.DETAIL_STATUS_USEING.getValue().replaceAll("DS", ""))); // 子状态为使用中
//						} else 
							if (DateUtils.isSameDay(date, goods.getEndDate())) {
        					goods.setDetailStatus(Byte.parseByte(GoodsStatusEnum.DETAIL_STATUS_TOD_DOWNLINE.getValue().replaceAll("DS", ""))); // 子状态为今日下线
        				} else if (goods.getEndDate().getTime() < date.getTime()) {
            				goods.setDetailStatus(Byte.parseByte(GoodsStatusEnum.DETAIL_STATUS_DOWNLINE.getValue().replaceAll("DS", ""))); // 子状态为已下线
            				status = GoodsStatusEnum.STATUS_CLOSE.getValue().replace("S", "");
            				goods.setStatus(Byte.parseByte(status));
    					} else {
        					Calendar calendar = Calendar.getInstance();
        					calendar.setTime(date);
        					calendar.add(Calendar.DATE, 1);
        					if (DateUtils.isSameDay(calendar.getTime(), goods.getEndDate())) {
        						goods.setDetailStatus(Byte.parseByte(GoodsStatusEnum.DETAIL_STATUS_TOM_DOWNLINE.getValue().replaceAll("DS", ""))); // 子状态为明日下线
        					} else {
        						goods.setDetailStatus(Byte.parseByte(GoodsStatusEnum.DETAIL_STATUS_USEING.getValue().replaceAll("DS", ""))); // 子状态为使用中
        					}
        				} 
        			} else if (goods.getBeginDate().getTime() > date.getTime()) {
        				goods.setDetailStatus(Byte.parseByte(GoodsStatusEnum.DETAIL_STATUS_USEING.getValue().replaceAll("DS", ""))); // 子状态为使用中
        			} else if (goods.getEndDate().getTime() < date.getTime()) {
        				goods.setDetailStatus(Byte.parseByte(GoodsStatusEnum.DETAIL_STATUS_DOWNLINE.getValue().replaceAll("DS", ""))); // 子状态为已下线
        				status = GoodsStatusEnum.STATUS_CLOSE.getValue().replace("S", "");
        				goods.setStatus(Byte.parseByte(status));
					}
    			}
    		}
//    		});
		}
    }

}
