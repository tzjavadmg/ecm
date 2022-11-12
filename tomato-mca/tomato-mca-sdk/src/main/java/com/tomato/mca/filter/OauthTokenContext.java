//package com.tomato.mca.filter;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 名    称：
// * 功    能：
// * 创 建 人：sailor
// * 创建时间：2017/11/2上午10:43
// * 修 改 人：
// * 修改时间：
// * 说    明：
// * 版 本 号：
// */
//public class OauthTokenContext {
//    private final static ThreadLocal<Map<String,Object>> oauthTokenMap = new ThreadLocal<Map<String, Object>>();
//
//    private final static String OAUTH_TOKEN = "oauthToken";
//
//    public static String getTokenVal(){
//        Map<String,Object> map = oauthTokenMap.get();
//        if (null != map){
//            Object token = map.get(OAUTH_TOKEN);
//            return token != null?token.toString():null;
//        }
//        return null;
//    }
//
//    protected static void setTokenVal(String tokenVal){
//        Map<String,Object> map = oauthTokenMap.get();
//        if (map == null){
//            map = new HashMap<String, Object>();
//        }
//        map.put(OAUTH_TOKEN,tokenVal);
//        oauthTokenMap.set(map);
//    }
//}
