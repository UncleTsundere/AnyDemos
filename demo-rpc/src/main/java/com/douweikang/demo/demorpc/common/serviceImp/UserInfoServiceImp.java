package com.douweikang.demo.demorpc.common.serviceImp;

import com.douweikang.demo.demorpc.common.dao.UserInfoDao;
import com.douweikang.demo.demorpc.common.entity.UserInfo;
import com.douweikang.demo.demorpc.common.service.UserInfoService;
import org.springframework.stereotype.Service;

@Service("UserInfoService")
public class UserInfoServiceImp implements UserInfoService {

    private final UserInfoDao userInfoDao;

    public UserInfoServiceImp(UserInfoDao userInfoDao) {
        this.userInfoDao = userInfoDao;
    }

    @Override
    public UserInfo getById(int id) throws Exception {
        return userInfoDao.getById(id);
    }
}
