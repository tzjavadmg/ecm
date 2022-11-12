package com.milisong.wechat.miniapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MiniAccessTokenDto {

    private String accessToken;

    private int expiresIn = -1;
}
