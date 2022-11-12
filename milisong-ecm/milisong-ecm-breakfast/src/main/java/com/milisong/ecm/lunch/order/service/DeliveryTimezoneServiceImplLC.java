package com.milisong.ecm.lunch.order.service;

import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.util.BeanMapper;
import com.milisong.ecm.common.dto.ShopConfigDto;
import com.milisong.ecm.common.util.DateTimeUtils;
import com.milisong.ecm.common.util.RedisKeyUtils;
import com.milisong.ecm.common.util.WeekDayUtils;
import com.milisong.oms.dto.TimezoneDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/23 20:08
 */
@Slf4j
@Component
public class DeliveryTimezoneServiceImplLC implements DeliveryTimezoneService {

    @Resource
    private ShopConfigServiceLC shopConfigServiceLC;

    @Override
    public ResponseResult<List<TimezoneDto>> getAllDeliveryTimezones(Byte businessLine, String shopCode) {
        List<ShopConfigDto.DeliveryTimezone> deliveryTimes = shopConfigServiceLC.getDeliveryTimezones(shopCode);
        return ResponseResult.buildSuccessResponse(BeanMapper.mapList(deliveryTimes, TimezoneDto.class));
    }

    @Override
    public ResponseResult<List<TimezoneDto>> getAvailableTimezones(Byte businessLine, String shopCode, Date deliveryDate) {
        List<ShopConfigDto.DeliveryTimezone> deliveryTimes = shopConfigServiceLC.getDeliveryTimezones(shopCode);
        List<TimezoneDto> availableTimezones = new ArrayList<>();
        boolean hasDefault = false;
        TimezoneDto firstAvaliableDeliveryTimezone = null;
        for (ShopConfigDto.DeliveryTimezone deliveryTime : deliveryTimes) {
            TimezoneDto deliveryTimezone = BeanMapper.map(deliveryTime, TimezoneDto.class);
            boolean isAvailable = isAvailable(shopCode, deliveryTimezone, deliveryDate);
            deliveryTimezone.setAvailable(isAvailable);
            boolean isDefault = deliveryTimezone.getIsDefault() != null && deliveryTimezone.getIsDefault();

            if (isAvailable && firstAvaliableDeliveryTimezone == null) {
                firstAvaliableDeliveryTimezone = deliveryTimezone;
            }
            if (isAvailable && isDefault) {
                hasDefault = true;
            } else {
                deliveryTimezone.setIsDefault(false);
            }
            availableTimezones.add(deliveryTimezone);
        }

        if (!hasDefault && firstAvaliableDeliveryTimezone != null) {
            firstAvaliableDeliveryTimezone.setIsDefault(true);
        }
        return ResponseResult.buildSuccessResponse(availableTimezones);
    }

    private boolean isAvailable(String shopCode, TimezoneDto deliveryTimezone, Date deliveryDate) {
        boolean isAvailable = true;
        Date current = new Date();
        Date cutOffDate = DateTimeUtils.mergeDateAndTimeString(current, deliveryTimezone.getCutoffTime());
        if (WeekDayUtils.isToday(deliveryDate) && current.compareTo(cutOffDate) > 0) {
            isAvailable = false;
        }

        Date deliveryStartTime = DateTimeUtils.mergeDateAndTimeString(deliveryDate, deliveryTimezone.getStartTime());
        if (!hasCapacity(shopCode, deliveryStartTime, deliveryTimezone.getMaxCapacity())) {
            isAvailable = false;
        }
        return isAvailable;
    }

    //TODO 重构至oms
    private boolean hasCapacity(String shopCode, Date deliveryStartTime, Integer maxCapacity) {
        String counterKey = RedisKeyUtils.getDeliveryCounterKey(shopCode, deliveryStartTime);
        String counterString = RedisCache.get(counterKey);
        Integer counter = 0;
        if (StringUtils.isNotEmpty(counterString)) {
            counter = new Integer(counterString);
        }
        return counter < maxCapacity;
    }
}
