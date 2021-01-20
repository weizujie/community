package com.community.controller;

import com.community.entity.Comment;
import com.community.service.CommentService;
import com.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    /**
     * 增加评论/回复
     *
     * @param discussPostId 帖子 id
     * @param comment       评论/回复
     */
    @PostMapping("/add/{discussPostId}")
    public String addComment(@PathVariable int discussPostId, Comment comment) {
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());

        commentService.insertComment(comment);
        return "redirect:/post/detail/" + discussPostId;

    }

}
