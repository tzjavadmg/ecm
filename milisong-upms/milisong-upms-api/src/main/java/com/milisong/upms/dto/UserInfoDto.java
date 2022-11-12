package com.milisong.upms.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 用户信息
 * <p>
 * Title: UserInfo<br>
 * Description: UserInfo<br>
 * CreateDate:2017年8月30日 下午4:51:58
 *
 * @author woody
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto implements Serializable {
    private static final long serialVersionUID = 80313579500871154L;
    private Integer userNo;
    private String accountName;
    private String nickname;
    private String userName;
    @JsonIgnore
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
    private String departmentName;
}
