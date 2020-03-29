package com.douweikang.demo.demorpc.common.service;

import com.douweikang.demo.demorpc.common.entity.UserInfo;

public interface UserInfoService {

    UserInfo getById(int id) throws Exception;
}
