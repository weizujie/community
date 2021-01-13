package com.community.mapper;

import com.community.entity.User;

public interface UserMapper {

    /**
     * 根据用户 id 查询用户
     *
     * @param id 用户id
     * @return User
     */
    User selectUserById(int id);
}
