package com.milisong.upms.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * sso的菜单tree的DTO
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class MenuDto implements Serializable{
    private static final long serialVersionUID = -860114144901676635L;
    private Integer id;
    private String name;
    private String code;
    private Integer type;
    private String path;
    private String icon;
    private String remark;
    private Integer pid;
    private Integer appId;
    private Integer sort;
    private Date createTime;
    private Date updateTime;
    private Boolean isDelete;
    private Boolean isPublic;
    private Integer terminal;
    private List<MenuDto> children = new ArrayList<>();
}
