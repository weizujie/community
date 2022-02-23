package com.community.service.impl;

import com.community.entity.Message;
import com.community.mapper.MessageMapper;
import com.community.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    /**
     * 查询当前用户的会话列表，针对每个会话返回一条最新的私信
     */
    @Override
    public List<Message> selectConversations(int userId, int offset, int limit) {
        return messageMapper.selectConversations(userId, offset, limit);
    }

    /**
     * 查询当前用户的会话数量
     */
    @Override
    public int selectConversationCount(int userId) {
        return messageMapper.selectConversationCount(userId);
    }

    /**
     * 查询某个会话所包含的私信列表
     */
    @Override
    public List<Message> selectLetters(String conversationId, int offset, int limit) {
        return messageMapper.selectLetters(conversationId, offset, limit);
    }

    /**
     * 查询某个会话所包含的私信数量
     */
    @Override
    public int selectLetterCount(String conversationId) {
        return messageMapper.selectLetterCount(conversationId);
    }

    /**
     * 查询未读私信的数量
     * conversationId 作为动态拼接条件，
     * 如果不拼接则查询所有的未读私信的数量，否则只查询某个用户的未读私信数量
     */
    @Override
    public int selectLetterUnreadCount(int userId, String conversationId) {
        return messageMapper.selectLetterUnreadCount(userId, conversationId);
    }

    /**
     * 新增消息
     */
    @Override
    public int insertMessage(Message message) {
        // HTML 转义
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        return messageMapper.insertMessage(message);
    }

    /**
     * 修改消息的状态，将所有消息变为已读
     */
    @Override
    public int updateStatus(List<Integer> ids) {
        return messageMapper.updateStatus(ids, 1);
    }

}
