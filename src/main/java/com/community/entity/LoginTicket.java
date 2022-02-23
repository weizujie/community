package com.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * 登录凭证（后面会改成 Redis）
 */
@Data
public class LoginTicket {


    private Integer id;

    private Integer userId;

    private String ticket;

    private Integer status;

    private Date expired;

}
