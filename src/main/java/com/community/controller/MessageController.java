package com.community.controller;

import com.community.entity.Message;
import com.community.entity.Page;
import com.community.entity.User;
import com.community.service.MessageService;
import com.community.service.UserService;
import com.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;


    /**
     * 私信列表
     */
    @GetMapping("/letter/list")
    public String getLetterList(Model model, Page page) {
        User curUser = hostHolder.getUser();
        // 分页信息
        page.setLimit(5);
        page.setPath("/letter/list");
        page.setRows(messageService.selectConversationCount(curUser.getId()));

        // 会话列表
        List<Message> conversationList = messageService.selectConversations(curUser.getId(), page.getOffset(), page.getLimit());

        List<Map<String, Object>> conversations = new ArrayList<>();
        if (conversationList != null) {
            for (Message message : conversationList) {
                Map<String, Object> map = new HashMap<>();
                map.put("conversation", message);
                map.put("letterCount", messageService.selectLetterCount(message.getConversationId()));
                map.put("unreadCount", messageService.selectLetterUnreadCount(curUser.getId(), message.getConversationId()));
                int targetId = curUser.getId() == message.getFromId() ? message.getToId() : message.getFromId();
                map.put("target", userService.selectById(targetId));

                conversations.add(map);
            }
        }
        model.addAttribute("conversations", conversations);

        // 未读消息数量
        int letterUnreadCount = messageService.selectLetterUnreadCount(curUser.getId(), null);
        model.addAttribute("letterUnreadCount", letterUnreadCount);
        return "site/letter";
    }


    @GetMapping("/letter/detail/{conversationId}")
    public String getLetterDetail(@PathVariable String conversationId, Page page, Model model) {
        // 分页信息
        page.setLimit(5);
        page.setPath("/letter/detail/" + conversationId);
        page.setRows(messageService.selectLetterCount(conversationId));

        // 私信列表
        List<Message> letterList = messageService.selectLetters(conversationId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> letters = new ArrayList<>();
        if (letterList != null) {
            for (Message letter : letterList) {
                Map<String, Object> map = new HashMap<>();
                map.put("letter", letter);
                map.put("fromUser", userService.selectById(letter.getFromId()));
                letters.add(map);
            }
        }
        model.addAttribute("letters", letters);

        // 私信目标
        String[] ids = conversationId.split("_");
        int id0 = Integer.parseInt(ids[0]);
        int id1 = Integer.parseInt(ids[1]);
        if (hostHolder.getUser().getId() == id0) {
            User target = userService.selectById(id1);
            model.addAttribute("target", target);
        } else {
            User target = userService.selectById(id0);
            model.addAttribute("target", target);
        }
        return "site/letter-detail";
    }

}
