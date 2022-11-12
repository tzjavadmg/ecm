package com.milisong.dms.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;

public class SysConstant {

    @Getter
	@AllArgsConstructor
    public static enum SYSTEM_INFO  {
        SUCCESS("6666", "成功！"),
        FALSE("4444", "失败！"),
        SYSTEM_ERROR("5555", "系统后台繁忙，请稍候一段时间再尝试...."),
        TIME_OUT_EXCEPTION("0500", "连接超时"),
        DATA_LOSS("0513", "信息不完整"),
        DATA_INVALID("0515", "信息不合法"),
        DATA_ERROR("0516", "信息不合法或者不合法"),
        DATA_REPETITION ("0514", "信息重复"),
		NO_MUCH_SUBMIT("5000","不许多次提交"),

        NOT_LOGIN("6008", "请先登录"),
        PERMISSION_DENIED("6009", "用户无操作权限"),
        INVALID_TICKET_ERROR("6010", "ticket已失效或不存在"),


	    RESOLVE_FILE_ERROR("7000", "解析文件失败"),
		FILE_TYPE_MISMATCH("7001", "文件格式不匹配"),

        FILE_FORMART_ERROR("30700","文件格式错误"),
        FILE_SIZE_ERROR("30701","文件大小超出限制范围"),
        FILE_OSS_ERROR("30702","文件上传OSS失败"),
        NO_SHOP_PERMISSION("30703","用户无该门店权限"),
        ;

        private String code;
        private String desc;

    }
}
