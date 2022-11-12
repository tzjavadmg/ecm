package com.milisong.oms.util;

import java.util.Date;

public class DateUtil {
	
    public static boolean computeTime(Date orderDate, Date currentDate,int unPayExpiredTime) {
        long interval = (currentDate.getTime() - orderDate.getTime()) / 1000;
        if (interval >= unPayExpiredTime) {
            return true;
        }
        return false;
    }
    public static boolean computeEndTime(Date deliveryEndTime,Date currentDate,int orderEndTime){
    	long interval = (currentDate.getTime() - deliveryEndTime.getTime())/(1000*60);
    	if (interval >= orderEndTime) {
    		return true;
    	}
    	return false;
    	
    }
}
