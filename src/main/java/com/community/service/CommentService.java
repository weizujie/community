package com.community.service;

import com.community.entity.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> selectCommentByEntity(int entityType, int entityId, int offset, int limit);

    int selectCountByEntity(int entityType, int entityId);

    int insertComment(Comment comment);

}
