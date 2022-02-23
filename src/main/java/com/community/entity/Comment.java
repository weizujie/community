package com.community.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("comment")
public class Comment {

    // 评论 id
    private Integer id;
    // 用户 id
    private Integer userId;
    // 实体类型  0-评论  1-回复（给评论的评论）
    private Integer entityType;
    // 实体 id
    private Integer entityId;
    // 回复或评论的目标用户
    private Integer targetId;
    // 正文
    private String content;
    // 状态  0-正常
    private Integer status;
    // 创建时间
    private Date createTime;


}
