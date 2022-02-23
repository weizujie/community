package com.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.entity.Message;

import java.util.List;

public interface MessageMapper extends BaseMapper<Message> {

    /**
     * 查询当前用户的会话列表，针对每个会话返回一条最新的私信
     */
    List<Message> selectConversations(int userId, int offset, int limit);

    /**
     * 查询当前用户的会话数量
     */
    int selectConversationCount(int userId);

    /**
     * 查询某个会话所包含的私信列表
     */
    List<Message> selectLetters(String conversationId, int offset, int limit);

    /**
     * 查询某个会话所包含的私信数量
     */
    int selectLetterCount(String conversationId);

    /**
     * 查询未读私信的数量
     * conversationId 作为动态拼接条件，
     * 如果不拼接则查询所有的未读私信的数量，否则只查询某个用户的未读私信数量
     */
    int selectLetterUnreadCount(int userId, String conversationId);

    /**
     * 新增消息
     */
    int insertMessage(Message message);

    /**
     * 修改消息的状态，将所有消息变为已读
     */
    int updateStatus(List<Integer> ids, int status);
}
