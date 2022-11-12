package com.milisong.delay.enums;

public enum SysEnum {
	SUCCESS																	("0000",   "操作成功"),
	OPERAT_FAILURE															("9090",   "操作失败"),
	SYSTEM_ERROR															("9999",   "系统后台繁忙，请稍候一段时间再尝试...."),
	TIME_OUT_EXCEPTION														("0500",   "连接超时"),	
	NET_WORK_EXCEPTION                                                      ("0600",   "网络异常，请稍后再试"),
	MESSAGE_IS_EXPIRE                                                       ("0700",   "消息已过期"),
	PARAM_VALIDATE_NULL 													("0999",   "参数不能为空");
	




	private String value;

    private String chName;

    private SysEnum(String value, String chName) {
        this.value = value;
        this.chName = chName;
    }

    public String getCode() {
        return value;
    }
    
    public String getName(){
        return chName;
    }
}
