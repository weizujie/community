package com.community.mapper;

import com.community.entity.LoginTicket;

@Deprecated
public interface LoginTicketMapper {

    /**
     * 新增用户凭证
     */
    int insertLoginTicket(LoginTicket loginTicket);

    /**
     * 根据 ticket 查询用户凭证
     */
    LoginTicket selectByTicket(String ticket);

    /**
     * 更新用户状态
     */
    int updateStatus(String ticket, int status);

}
