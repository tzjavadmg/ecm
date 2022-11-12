package com.milisong.dms.dto.shunfeng;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author zhaozhonghui
 * @date 2018-10-23
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SfOrderReceiveDto implements Serializable {
    private static final long serialVersionUID = -4106345013802134747L;
    /** 用户名 */
    @NotNull
    private String userName;
    /** 用户电话 */
    @NotNull
    private String userPhone;
    /** 用户地址  */
    @NotNull
    private String userAddress;
    /** 用户经度  */
    @NotNull
    private String userLng;
    /** 用户维度  */
    @NotNull
    private String userLat;
}
