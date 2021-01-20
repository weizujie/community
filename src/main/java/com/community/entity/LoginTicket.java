package com.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 登录凭证（后面会改成 Redis）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginTicket {

    // 凭证 id
    private int id;
    // 用户 id
    private int userId;
    // 用户凭证
    private String ticket;
    // 状态  0-有效  1-无效
    private int status;
    // 凭证过期时间
    private Date expired;

}
