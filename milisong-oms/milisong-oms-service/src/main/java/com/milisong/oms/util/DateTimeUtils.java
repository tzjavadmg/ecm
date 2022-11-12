package com.milisong.oms.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/23 20:48
 */
public class DateTimeUtils {

    public static Date mergeDateAndTimeString(Date date,String timeString) {
        LocalTime cutoffTime = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));

        ZoneId zoneId = ZoneOffset.systemDefault();
        LocalDate localDate = date.toInstant().atZone(zoneId).toLocalDate();

        Instant instant = LocalDateTime.of(localDate, cutoffTime).atZone(zoneId).toInstant();
        return Date.from(instant);
    }

}
