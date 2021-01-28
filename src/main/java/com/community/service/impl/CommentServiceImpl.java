package com.community.service.impl;

import com.community.entity.Comment;
import com.community.mapper.CommentMapper;
import com.community.mapper.DiscussPostMapper;
import com.community.service.CommentService;
import com.community.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    /**
     * 根据实体查询评论
     *
     * @param entityType 实体类型 0-帖子  1-评论
     * @param entityId   实体 id
     * @param offset     每页起始行行号
     * @param limit      一页显示多少条数据
     */
    @Override
    public List<Comment> selectCommentByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentByEntity(entityType, entityId, offset, limit);
    }

    /**
     * 根据实体查询评论数
     *
     * @param entityType 实体类型 0-帖子  1-评论
     * @param entityId   实体 id
     */
    @Override
    public int selectCountByEntity(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }

    /**
     * 增加评论
     * 在 comment 表里新增评论，然后在 discuss_post 表里更新评论数量，需要事务来管理，新增评论成功必须更新评论数量，要么全部执行，要么全部不执行
     * isolation = Isolation.READ_COMMITTED：事务的隔离级别。读取已提交
     * propagation = Propagation.REQUIRED：事务的传播。支持当前事务，如果当前没有事务，就新建一个事务
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int insertComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        // HTML 字符转义
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        // 新增评论
        int rows = commentMapper.insertComment(comment);

        // 更新帖子评论数量
        if (comment.getEntityType() == Constant.ENTITY_TYPE_POST) {
            int count = commentMapper.selectCountByEntity(Constant.ENTITY_TYPE_POST, comment.getEntityId());
            discussPostMapper.updateCommentCount(comment.getEntityId(), count);
        }
        return rows;
    }

    /**
     * 根据用户 id 查询评论数量
     */
    @Override
    public int selectCountByUserId(int userId) {
        return commentMapper.selectCountByUserId(userId);
    }

    /**
     * 根据用户 id 查询评论
     */
    @Override
    public List<Comment> selectCommentByUserId(int userId, int offset, int limit) {
        return commentMapper.selectCommentByUserId(userId, offset, limit);
    }
}
