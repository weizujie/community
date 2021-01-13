package com.community.service.impl;

import com.community.entity.User;
import com.community.mapper.UserMapper;
import com.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 根据用户 id 查询用户
     *
     * @param id 用户id
     * @return User
     */
    @Override
    public User selectUserById(int id) {
        return userMapper.selectUserById(id);
    }
}
