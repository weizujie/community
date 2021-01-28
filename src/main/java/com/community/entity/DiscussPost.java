package com.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscussPost {

    // 帖子 id
    private Integer id;
    // 用户 id
    private Integer userId;
    // 标题
    private String title;
    // 内容
    private String content;
    // 分类  0-普通    1-置顶
    private Integer type;
    // 状态  0-正常    1-精华    2-拉黑
    private Integer status;
    // 评论数（冗余的写在这里，正确做法应该是在 comment 表里，但效率低）
    private Integer commentCount;
    // 分数 用于计算热贴排行
    private Double score;
    // 创建时间
    private Date createTime;
}
