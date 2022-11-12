package com.milisong.upms.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;

public class SsoErrorConstant {

    @Getter
	@AllArgsConstructor
    public static enum SYSTEM  {
        SUCCESS("200", "成功！"),
        FALSE("500", "失败！"),
        SYSTEM_ERROR("9999", "系统后台繁忙，请稍候一段时间再尝试...."),
        TIME_OUT_EXCEPTION("0500", "连接超时"),
        DATA_LOSS("0513", "信息不完整"),
        DATA_INVALID("0515", "信息不合法"),
        DATA_ERROR("0516", "信息不合法或者不合法"),

        NOT_LOGIN("6008", "请先登录"),
        PERMISSION_DENIED("6009", "用户无操作权限")
        ;

        private String code;
        private String desc;

    }
}
