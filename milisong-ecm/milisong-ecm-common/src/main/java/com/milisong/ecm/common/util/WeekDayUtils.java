package com.milisong.ecm.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;
import org.joda.time.Period;

public class WeekDayUtils {
    public static String getWeekDayString(Date date) {
        String dayDesc = null;
        Date today = DateUtils.truncate(new Date(), Calendar.DATE);
        DateTime dateTime = new DateTime(date);
        if (date.compareTo(today) >= 0) {
            Interval interval = new Interval(today.getTime(), date.getTime());
            Period period = interval.toPeriod();
            int dayNo = period.getDays();
            int weeks = period.getWeeks();
            if (dayNo == 0 && weeks == 0) {
                dayDesc = "今天";
            }
            if (dayNo == 1 && weeks == 0) {
                dayDesc = "明天";
            }
        }

        if (dayDesc == null) {
            switch (dateTime.getDayOfWeek()) {
                case DateTimeConstants.SUNDAY:
                    dayDesc = "周日";
                    break;
                case DateTimeConstants.MONDAY:
                    dayDesc = "周一";
                    break;
                case DateTimeConstants.TUESDAY:
                    dayDesc = "周二";
                    break;
                case DateTimeConstants.WEDNESDAY:
                    dayDesc = "周三";
                    break;
                case DateTimeConstants.THURSDAY:
                    dayDesc = "周四";
                    break;
                case DateTimeConstants.FRIDAY:
                    dayDesc = "周五";
                    break;
                case DateTimeConstants.SATURDAY:
                    dayDesc = "周六";
                    break;
                default:
            }
        }
        return dayDesc;
    }

    public static int [] getTodayOrTomorrow(String date) {
    	int [] arr = new int[2];
        Date compareDate = null;
        try {
            compareDate = DateUtils.parseDate(date, "yyyy-MM-dd");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date today = DateUtils.truncate(DateUtils.truncate(new Date(), Calendar.DATE), Calendar.DATE);
        if (compareDate.compareTo(today) < 0) {
            arr [0] = -1;
            return arr;
        }
        Interval interval = new Interval(today.getTime(), compareDate.getTime());
        Period period = interval.toPeriod();
        int dayNo = period.getDays();
        int weeks = period.getWeeks();
        arr[0] = dayNo;
        arr[1] = weeks;
        return arr;
    }

    public static boolean isToday(Date compareDate) {
        Calendar currentCal = Calendar.getInstance();
        Calendar compareCal = Calendar.getInstance();
        compareCal.setTime(compareDate);

        return currentCal.get(Calendar.YEAR) == compareCal.get(Calendar.YEAR) && currentCal.get(Calendar.MONTH) == compareCal.get(Calendar.MONTH) && currentCal.get(Calendar.DATE) == compareCal.get(Calendar.DATE);
    }

    public static boolean isTomorrow(Date compareDate) {
        Calendar currentCal = Calendar.getInstance();
        Calendar compareCal = Calendar.getInstance();
        compareCal.setTime(compareDate);

        return currentCal.get(Calendar.YEAR) == compareCal.get(Calendar.YEAR) && currentCal.get(Calendar.MONTH) == compareCal.get(Calendar.MONTH) && (currentCal.get(Calendar.DATE) + 1 == compareCal.get(Calendar.DATE));
    }

    
    public static Integer getWeekOfYear(Date date) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setFirstDayOfWeek(Calendar.MONDAY);
    	calendar.setTime(date);
    	return calendar.get(Calendar.WEEK_OF_YEAR);
    }
    
    public static boolean isThisTime(long time ,String pattern) {
    	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    	Date date = new Date(time);
        String param = sdf.format(date);//参数时间
        String now = sdf.format(new Date());//当前时间
        if (param.equals(now)) {
        	return true;
        }
        return false;
    }


}
