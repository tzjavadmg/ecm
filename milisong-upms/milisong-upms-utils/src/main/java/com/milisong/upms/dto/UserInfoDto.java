package com.milisong.upms.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 用户信息
 * @author yangzhilong
 *
 */
@Data
public class UserInfoDto implements Serializable {
    private static final long serialVersionUID = 80313579500871157L;
    private Long userNo;
    private String accountName;
    private String nickname;
    private String userName;
    private String password;
    private String mobile;
    private String email;
    private Integer gender;
    private String departmentCode;
    private Date joinTime;
    private String remark;
    private Date createTime;
    private Date updateTime;
    private Integer status;
    private Boolean isLeader;
    private Boolean isDelete;
    // 部门名称
    private String departmentName;
}
