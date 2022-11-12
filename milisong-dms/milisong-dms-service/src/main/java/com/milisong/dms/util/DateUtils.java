package com.milisong.dms.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * @author zhaozhonghui
 * @Description 日期工具类
 * @date 2018-12-26
 */
public class DateUtils {

    public static Date getDayBeging() {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = LocalDate.now();

        ZonedDateTime zonedDateTime = localDate.atStartOfDay(zoneId);
        Date dayBegdin = Date.from(zonedDateTime.toInstant());
        return dayBegdin;
    }

    public static Date getDayEnd() {
        Date dayBeging = getDayBeging();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dayBeging);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.add(Calendar.SECOND, -1);
        return calendar.getTime();
    }

    public static Date getLastDayBeging(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 格式化日期
     * @param date
     * @return
     */
    public static String format(Date date, String format) {
    	DateFormat df = new SimpleDateFormat(format);
    	return df.format(date);
    }

    /**
     * 将字符串变成日期
     * @param dateStr
     * @param format
     * @return
     */
    public static Date toDate(String dateStr, String format) {
    	DateFormat df = new SimpleDateFormat(format);
    	try {
			return df.parse(dateStr);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
    }

    /**
     * 获取当前时间的HHmm形式的字符串
     * @return
     */
    public static String getNowTimeWithHHmmStr() {
    	StringBuilder sb = new StringBuilder();

    	Calendar cal = Calendar.getInstance();
    	sb.append(cal.get(Calendar.HOUR_OF_DAY)).append(cal.get(Calendar.MINUTE));

    	return sb.toString();
    }

    public static Date time2Date(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        Calendar resultCalendar = Calendar.getInstance();
        resultCalendar.setTime(getDayBeging());
        resultCalendar.set(Calendar.HOUR,calendar.get(Calendar.HOUR_OF_DAY));
        resultCalendar.set(Calendar.MINUTE,calendar.get(Calendar.MINUTE));
        resultCalendar.set(Calendar.SECOND,calendar.get(Calendar.SECOND));
        return resultCalendar.getTime();
    }
}
