package com.milisong.ucs.util;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/26 9:22
 */
public class UserRedisKeyUtils {

    static public String getLatestDeliveryAddressKey(Long userId) {
        return "latest_delivery_address:" + userId;
    }
    
    
    static public String getCompanyKey (Long buildId,String floor,String companyName) {
    	return "company:"+buildId+":"+floor+":"+companyName;
    }
    
    static public String getUserCountKey(Long userId){
    	return "same_company_hint_count:"+userId;
    }
    
    static public String getCompanySearchKey(Long buildId,String floor){
    	return "company:"+buildId+":"+floor+":*"; 
    }
}
