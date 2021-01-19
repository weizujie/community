package com.community.controller;

import com.community.entity.DiscussPost;
import com.community.entity.User;
import com.community.service.DiscussPostService;
import com.community.utils.CommonUtil;
import com.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/post")
public class DiscussPostController {

    private final DiscussPostService discussPostService;
    private final HostHolder hostHolder;

    public DiscussPostController(DiscussPostService discussPostService, HostHolder hostHolder) {
        this.discussPostService = discussPostService;
        this.hostHolder = hostHolder;
    }


    @PostMapping("/add")
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User loginUser = hostHolder.getUser();
        if (loginUser == null) {
            return CommonUtil.getJsonString(-1, "请登录后再操作!");
        }

        DiscussPost post = new DiscussPost();
        post.setUserId(loginUser.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.insertDiscussPost(post);
        return CommonUtil.getJsonString(0, "发布成功!");
    }


}


