package com.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * 帖子 实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscussPost {

    // 帖子 id
    private int id;
    // 用户 id
    private int userId;
    // 标题
    private String title;
    // 内容
    private String content;
    // 分类  0-普通    1-置顶
    private int type;
    // 状态  0-正常    1-精华    2-拉黑
    private int status;
    // 评论数
    private int commentCount;

    private double score;
    // 创建时间
    private Date createTime;
}
