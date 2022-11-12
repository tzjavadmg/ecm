package com.milisong.delay.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	public static final String format = "yyyy-MM-dd HH:mm:ss";
	
	public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
	}

}
