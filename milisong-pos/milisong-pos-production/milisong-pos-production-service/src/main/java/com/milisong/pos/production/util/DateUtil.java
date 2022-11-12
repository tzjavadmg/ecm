package com.milisong.pos.production.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 日期工具类
 * @author yangzhilong
 *
 */
@Slf4j
public class DateUtil {
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String HHMMSS = "HH:mm:ss";
	
	/**
	 * 格式化日期
	 * @param dateStr
	 * @param parsePattern
	 * @return
	 */
	public static Date parseDate(String dateStr, String parsePattern) {
		DateFormat df = new SimpleDateFormat(parsePattern);
		try {
			return df.parse(dateStr);
		} catch (ParseException e) {
			log.error("日期格式化错误", e);
		}
		return null;
	}
	
	/**
	 * 得到当前时间HH:mm:ss
	 * @return
	 */
	public static final Date getNowDateTime() {
		DateFormat df = new SimpleDateFormat(HHMMSS);
		String nowTime = df.format(DateUtils.setSeconds(new Date(), 0));
		Date nowDate = null;
		try {
			nowDate = df.parse(nowTime);
			return nowDate;
		} catch (ParseException e1) {
			log.error("日期格式化错误", e1);
			return null;
		}
	}
	
	/**
	 * 得到当前日期的yyyy-MM-dd
	 * @return
	 */
	public static final String getTodayYyyyMmDdStr() {
		return formatNowDate(YYYY_MM_DD);
	}
	
	/**
	 * 得到当前日期的
	 * @return
	 */
	public static final String formatNowDate(String format) {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(new Date());
	}
	
	/**
	 * 得到某个日期的开始时间 yyyy-MM-dd 00:00:00
	 * @param date
	 * @return
	 */
	public static final Date getDayBeginTime(Date date) {
		Calendar cal = DateUtils.toCalendar(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	/**
	 * 得到某个日期+incrementDay天的开始时间 yyyy-MM-dd 00:00:00
	 * @param date
	 * @param incrementDay
	 * @return
	 */
	public static final Date getDayBeginTime(Date date, int incrementDay) {
		Calendar cal = DateUtils.toCalendar(date);
		cal.add(Calendar.DAY_OF_YEAR, incrementDay);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
}
