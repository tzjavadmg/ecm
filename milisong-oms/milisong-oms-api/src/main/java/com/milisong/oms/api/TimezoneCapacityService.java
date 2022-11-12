package com.milisong.oms.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/11/12 10:21
 */
public interface TimezoneCapacityService {

    /**
     * 锁定门店配送时段产能
     *
     * @param shopTimezoneCapacityList 门店配送时段产能
     */
    void lockTimezoneCapacity(List<ShopTimezoneCapacity> shopTimezoneCapacityList);

    /**
     * 解锁门店配送时段产能
     *
     * @param shopTimezoneCapacityList 门店配送时段产能
     */
    void unlockTimezoneCapacity(List<ShopTimezoneCapacity> shopTimezoneCapacityList);

    /**
     * 增加门店配送时段产能
     *
     * @param shopTimezoneCapacityList 门店配送时段产能
     */
    void incrementTimezoneCapacity(List<ShopTimezoneCapacity> shopTimezoneCapacityList);

    /**
     * 扣减门店配送时段产能
     *
     * @param shopTimezoneCapacityList 门店配送时段产能
     */
    void decrementTimezoneCapacity(List<ShopTimezoneCapacity> shopTimezoneCapacityList);

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class ShopTimezoneCapacity {
        private Byte source;
        private Long orderId;
        private Long deliveryId;
        private String shopCode;
        private Date saleDate;
        private int goodsCount;
    }
}
