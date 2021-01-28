package com.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private Integer id;
    // 消息发送用户 id（ fromId为1表示系统用户，发送的不是私信而是通知）
    private Integer fromId;
    // 消息接收用户 id
    private Integer toId;
    // 会话 id（冗余字段，为了方便查询）
    private String conversationId;
    // 内容
    private String content;
    // 状态   0-未读   1-已读   2-删除
    private Integer status;
    // 创建时间
    private Date createTime;


}
