package com.milisong.delay.util;

public class RedisKeyUtil {
	
	public static final String CONSUME_MEESAGE_ID = "consume_meesageid";
	
	public static final String MESSAGE_EXPIRE = "message_expire";
	
	public static final String BASIS_DATA = "basis_data";
	
	public static final String INCR_TASK = "incr_task";
	
	public static final String INCR_MESSAGE = "incr_message";
	
	public static final String CALL_BACK = "call_back";
	/**
	 * 获取生产消息key
	 * @param msgId
	 * @return
	 */
    public static String getConsumeMessageIdKey(String msgId) {
        return CONSUME_MEESAGE_ID + ":" + msgId;
    }
    
    /**
     * 获取过期消息key
     * @param msgId
     * @return
     */
    public static String getMessageExpireKey(String msgId) {
    	return MESSAGE_EXPIRE + ":" +msgId;
    }
    
    /**
     * 获取redis消息，用于存放回调地址
     * @param key
     * @return
     */
    public static String getBasisDataKey(String key) {
    	return BASIS_DATA + "_" +key;
    }
    
    /**
     * 用于定时任务并发执行
     * @return
     */
    public static String getIncrTaskKey() {
    	return INCR_TASK;
    }
    
    public static String getIncrCallBackKey(String value) {
    	return CALL_BACK + ":" + value;
    }
    
    /**
     * 用于记录消息的唯一性
     * @return
     */
    public static String getIncrMesssageKey(String system,String module,Long bizId) {
    	return INCR_MESSAGE + ":" + system + ":" + module + ":" + bizId;
    }
    
    /**
     * 获取消息唯一id
     * @param bizId
     * @param incr
     * @return
     */
    public static String getMessageId(String system,String module,Long bizId,Long incr) {
    	return system + ":" + module +":" + bizId + ":" + incr;
    }
	
}
