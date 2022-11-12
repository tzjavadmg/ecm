package com.milisong.scm.printer.util.jiabo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
*@author    created by benny
*@date  2018年9月14日---下午6:52:06
*
*/
public class StringUtils {

	public static String convertMobile(String mobile) {
		if (mobile != null && !mobile.equals("")) {
			String usermobile1 = mobile.substring(0, mobile.length() -  
					(mobile.substring(3)).length()) + "****" + mobile.substring(7);
			return usermobile1;
		}
		return mobile;
	}
	public static String convertMobileLas(String mobile) {
		if (mobile != null && !mobile.equals("")) {
			String usermobile1 = mobile.substring(7);
			return usermobile1;
		}
		return mobile;
	}
	
	/**
	 * 格式化集单号
	 * @param setNo
	 * @return
	 */
	public static String subStringSetNo(String setNo) {
		if(org.apache.commons.lang3.StringUtils.isBlank(setNo)) {
			return "";
		}
		String number = setNo.substring(1, setNo.length());
		return "#"+Integer.parseInt(number);
	}
	
	/**
	 * 校验集单是否为当天的
	 * @param setNo
	 * @return
	 */
	public static boolean checkSetNo(String setNo) {
		if(org.apache.commons.lang3.StringUtils.isBlank(setNo)) {
			return true;
		}
		String nowSetNoTime = setNo.substring(1, 9);
		SimpleDateFormat smf = new SimpleDateFormat("yyyyMMdd");
		String nowDate = smf.format(new Date());
		if(!nowDate.equals(nowSetNoTime)) {
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		String setNo = "F20190211000002";
		System.out.println(checkSetNo(setNo));
		
	}
}
