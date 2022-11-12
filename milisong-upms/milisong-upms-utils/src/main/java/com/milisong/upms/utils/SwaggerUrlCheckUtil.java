package com.milisong.upms.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 校验是否是swagger的url
 * @author yangzhilong
 *
 */
public class SwaggerUrlCheckUtil {
	private static final List<String> swaggerUrlList = new ArrayList<>();
	static {
		swaggerUrlList.add("/swagger");
		swaggerUrlList.add("/webjars");
		swaggerUrlList.add("/api-docs");
	}

	/**
	 * 判断是否是swagger的url
	 * 
	 * @param requestUri
	 * @return
	 */
	public static boolean check(String requestUri) {
		for (String item : swaggerUrlList) {
			if (requestUri.contains(item)) {
				return true;
			}
		}
		return false;
	}
}
