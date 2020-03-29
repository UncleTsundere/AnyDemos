package com.douweikang.demo.demorpc.website.controller;

import com.douweikang.demo.demorpc.common.entity.UserInfo;
import com.douweikang.demo.demorpc.common.service.UserInfoService;
import com.douweikang.demo.demorpc.customannotation.RPCService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class HomeController {

    @RPCService
    private UserInfoService userInfoService;

    @RequestMapping("/Index")
    public UserInfo index(int id) throws Exception {

        UserInfo entity = null;

        try {
            entity = userInfoService.getById(id);
        } catch (Exception e) {

        }

        return entity;
    }
}
