package com.community.service.impl;

import com.community.entity.Comment;
import com.community.mapper.CommentMapper;
import com.community.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public List<Comment> selectCommentByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentByEntity(entityType, entityId, offset, limit);
    }

    @Override
    public int selectCountByEntity(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }
}
