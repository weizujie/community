package com.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DiscussPostMapper extends BaseMapper<DiscussPost> {

    /**
     * 查询用户帖子列表
     *
     * @param userId 用户 id
     * @param offset 每页起始行行号
     * @param limit  一页显示多少条数据
     * @return List<DiscussPost>
     */
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);


    /**
     * 查询帖子的行数
     * Param()注解：用于给参数取别名。如果只有一个参数，并且在 <if> 里使用，则必须加别名
     *
     * @param userId 用户 id
     * @return 帖子行数
     */
    int selectDiscussPostRows(@Param("userId") int userId);

    /**
     * 发布帖子
     */
    int insertDiscussPost(DiscussPost discussPost);

    /**
     * 查询帖子详情
     */
    DiscussPost selectDiscussPostById(int id);

    /**
     * 更新帖子的评论数量
     *
     * @param id           帖子 id
     * @param commentCount 评论数量
     */
    int updateCommentCount(int id, Long commentCount);

    /**
     * 查询某用户帖子数量
     *
     * @param userId 用户 id
     */
    int selectCountByUserId(int userId);
}
