package com.douweikang.demo.demorpc.common.dao;

import com.douweikang.demo.demorpc.common.entity.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoDao {

    @Select("SELECT * FROM userinfo WHERE id = #{id};")
    UserInfo getById(@Param("id") int id) throws Exception;
}
