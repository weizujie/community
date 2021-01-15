package com.community.service.impl;


import com.community.entity.DiscussPost;
import com.community.mapper.DiscussPostMapper;
import com.community.service.DiscussPostService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostServiceImpl implements DiscussPostService {

    private final DiscussPostMapper discussPostMapper;

    public DiscussPostServiceImpl(DiscussPostMapper discussPostMapper) {
        this.discussPostMapper = discussPostMapper;
    }

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


}
