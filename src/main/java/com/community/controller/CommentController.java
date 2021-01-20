package com.community.controller;

import com.community.entity.Comment;
import com.community.service.CommentService;
import com.community.utils.HostHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final HostHolder hostHolder;

    public CommentController(CommentService commentService, HostHolder hostHolder) {
        this.commentService = commentService;
        this.hostHolder = hostHolder;
    }


    @PostMapping("/add/{discussPostId}")
    public String addComment(@PathVariable int discussPostId, Comment comment) {
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());

        commentService.insertComment(comment);
        return "redirect:/post/detail/" + discussPostId;

    }

}
