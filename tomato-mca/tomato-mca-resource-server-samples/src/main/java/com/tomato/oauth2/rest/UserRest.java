package com.tomato.oauth2.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRest {

    @GetMapping("api/v1/user")
    public String getUserInfo(String username){
        return "Hello !"+username;
    }
}
