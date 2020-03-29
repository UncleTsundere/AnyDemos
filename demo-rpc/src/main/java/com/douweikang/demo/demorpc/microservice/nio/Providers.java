package com.douweikang.demo.demorpc.microservice.nio;

import com.douweikang.demo.demorpc.common.service.UserInfoService;

public class Providers {

    private UserInfoService userInfoService;

    public Providers(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    public UserInfoService getUserInfoService() {
        return userInfoService;
    }
}