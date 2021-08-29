package com.so.payload.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginReq {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public boolean isRememberMe;

    private String captcha;
}