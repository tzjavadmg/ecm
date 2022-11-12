package com.milisong.scm.base.util;

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
	public static final String YYYY = "yyyy";
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
	 * 得到当前时间的00秒
	 * @return
	 */
	public static final Date getNowDateTimeFormatStart() {
		Calendar c =Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	
	/**
	 * 获取当前时间的下一分钟00秒
	 * @return
	 */
	public static final Date getNowDateTimeFormatEnd() {
		Calendar c =Calendar.getInstance();
		c.setTime(new Date());
		int minute = c.get(Calendar.MINUTE);
		c.set(Calendar.MINUTE, minute+1);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
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
	 * 得到当前时间
	 * @return
	 */
	public static final Date getNowDateTime2() {
		 return new Date();
	}
	
	public static final Integer getyearByDate(Date date) {
		Calendar cal = DateUtils.toCalendar(date);
		  int year = cal.get(Calendar.YEAR);
		  return year;
	}
	
	/**
	 * 得到当前日期的yyyy-MM-dd
	 * @return
	 */
	public static final String getTodayYyyyMmDdStr() {
		return formatNowDate(YYYY_MM_DD);
	}
	/**
	 * 得到当前日期的yyyy-MM-dd
	 * @return
	 */
	public static final String getTodayYyyy() {
		return formatNowDate(YYYY);
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
	
	/**
	 * 格式化日期
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static final Date getDateParse(String date,String pattern) {
		DateFormat df = new SimpleDateFormat(pattern);
		try {
			Date parseDate = df.parse(date);
			return parseDate;
		} catch (ParseException e) {
			log.error("日期转换异常{}",e);
		}
		return null;
	}
	
	/**
	 * 比较可售时间是否小于等于最后结束时间
	 * @param date1 可售时间
	 * @param date2 最后结束时间
	 * @return
	 */
	public static final boolean compare_date(Date date1,Date date2,Date date3) {
		if(date1.getTime() == date2.getTime()||date3.getTime() == date1.getTime()) {
			return true;
		}else if(date1.getTime() < date2.getTime() && date1.getTime()> date3.getTime()){
			return true;
		}
		return false;
	}
	
	/**
	 * 格式化日期
	 * @param date
	 * @param format
	 * @return
	 */
	public static final String formatDate(Date date, String format) {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}
}
