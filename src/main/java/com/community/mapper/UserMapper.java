package com.community.mapper;

import com.community.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface UserMapper {

    /**
     * 根据用户 id 查询用户
     */
    User selectById(@Param("id") int id);

    /**
     * 根据用户名查询用户
     */
    User selectByUsername(@Param("username") String username);


    /**
     * 根据邮箱查询用户
     */
    User selectByEmail(String email);

    /**
     * 添加用户
     */
    void insertUser(User user);

    /**
     * 注册用户
     */
    Map<String, Object> register(User user);


    /**
     * 修改用户状态
     */
    void updateStatus(@Param("id") int userId, int status);

    /**
     * 激活用户
     */
    int activation(int userId, String code);

}
