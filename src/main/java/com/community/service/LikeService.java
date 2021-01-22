package com.community.service;

public interface LikeService {

    /**
     * 点赞
     */
    void like(int userId, int entityType, int entityId, int entityUserId);

    /**
     * 查询某实体点赞的数量
     */
    long selectEntityLikeCount(int entityType, int entityId);

    /**
     * 查询某用户对某实体点赞的状态
     */
    int selectEntityLikeStatus(int userId, int entityType, int entityId);

    /**
     * 查询某个用户获得的赞
     */
    int selectUserLikeCount(int userId);
}
