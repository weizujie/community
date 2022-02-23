package com.community.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user")
public class User {

    // 用户 id
    private Integer id;
    // 用户名
    private String username;
    // 密码
    private String password;
    // 加密盐
    private String salt;
    // 邮箱
    private String email;
    // 用户类别   0-普通用户    1-超级管理员    2-版主
    private Integer type;
    // 状态      0-未激活      1-已激活
    private Integer status;
    // 激活码
    private String activationCode;
    // 头像
    private String headerUrl;
    // 创建时间
    private Date createTime;

}
