package com.community.service.impl;


import com.community.entity.DiscussPost;
import com.community.mapper.DiscussPostMapper;
import com.community.service.DiscussPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class DiscussPostServiceImpl implements DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    /**
     * 查询用户帖子列表
     *
     * @param userId 用户 id
     * @param offset 每页起始行行号
     * @param limit  一页显示多少条数据
     * @return List<DiscussPost>
     */
    @Override
    public List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit) {
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    /**
     * 查询帖子的行数
     *
     * @param userId 用户 id
     * @return 帖子行数
     */
    @Override
    public int selectDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    /**
     * 发布帖子
     */
    @Override
    public int insertDiscussPost(DiscussPost discussPost) {
        if (discussPost == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        // 转义 HTML 标签
        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));

        return discussPostMapper.insertDiscussPost(discussPost);
    }

    /**
     * 查询帖子详情
     */
    @Override
    public DiscussPost selectDiscussPostById(int id) {
        return discussPostMapper.selectDiscussPostById(id);
    }

    /**
     * 更新帖子的评论数量
     *
     * @param id           帖子 id
     * @param commentCount 评论数量
     */
    @Override
    public int updateCommentCount(int id, int commentCount) {
        return discussPostMapper.updateCommentCount(id, commentCount);
    }

}
