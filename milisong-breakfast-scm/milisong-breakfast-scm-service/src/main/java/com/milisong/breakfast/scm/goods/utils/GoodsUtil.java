package com.milisong.breakfast.scm.goods.utils;

/**
 * 商品工具类
 * @author youxia 2018年9月4日
 */
public class GoodsUtil {
	
	/**
	 * 生成商品编码
	 * @return
	 * youxia 2018年9月4日
	 */
    public static String generateGoodsCode(){
        StringBuilder sb = new StringBuilder();
        int first = (int)(1+(Math.random()*9));
        sb.append(first);
        for(int i = 0; i < 10; i++) {
            sb.append((int)(Math.random()*9));
        }
        return sb.toString();
    }
    
    public static String generateGoodsCategroyCode(){
        StringBuilder sb = new StringBuilder();
        int first = (int)(1+(Math.random()*9));
        sb.append("H"+first);
        for(int i = 0; i < 3; i++) {
            sb.append((int)(Math.random()*9));
        }
        return sb.toString();
    }
    
}
