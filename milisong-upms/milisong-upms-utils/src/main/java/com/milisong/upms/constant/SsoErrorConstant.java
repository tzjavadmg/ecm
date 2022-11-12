package com.milisong.upms.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;

public class SsoErrorConstant {

    @Getter
	@AllArgsConstructor
    public static enum SYSTEM_INFO  {
    	FAIL("500", "请求未得到任何返回值！"),
        NOT_LOGIN("6008", "请先登录"),
        PERMISSION_DENIED("6009", "用户无操作权限"),
        NO_SHOP_PERMISSION("6010","用户无该门店权限")
        ;

        private String code;
        private String desc;

    }
}
