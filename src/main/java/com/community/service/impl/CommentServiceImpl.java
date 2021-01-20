package com.community.service.impl;

import com.community.entity.Comment;
import com.community.mapper.CommentMapper;
import com.community.mapper.DiscussPostMapper;
import com.community.service.CommentService;
import com.community.utils.Constant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final DiscussPostMapper discussPostMapper;

    public CommentServiceImpl(CommentMapper commentMapper, DiscussPostMapper discussPostMapper) {
        this.commentMapper = commentMapper;
        this.discussPostMapper = discussPostMapper;
    }

    @Override
    public List<Comment> selectCommentByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentByEntity(entityType, entityId, offset, limit);
    }

    @Override
    public int selectCountByEntity(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int insertComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        // HTML 字符转义
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        int rows = commentMapper.insertComment(comment);

        // 更新帖子评论数量
        if (comment.getEntityType() == Constant.ENTITY_TYPE_POST) {
            int count = commentMapper.selectCountByEntity(Constant.ENTITY_TYPE_POST, comment.getEntityId());
            discussPostMapper.updateCommentCount(comment.getEntityId(), count);
        }

        return rows;
    }
}
