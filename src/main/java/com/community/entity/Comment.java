package com.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    // 评论 id
    private int id;
    // 用户 id
    private int userId;
    // 实体类型  0-评论  1-回复（给评论的评论）
    private int entityType;
    // 实体 id
    private int entityId;
    // 回复或评论的目标用户
    private int targetId;
    // 正文
    private String content;
    // 状态  0-正常
    private int status;
    // 创建时间
    private Date createTime;


}
