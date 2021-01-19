package com.community.controller;

import com.community.entity.DiscussPost;
import com.community.entity.User;
import com.community.service.DiscussPostService;
import com.community.service.UserService;
import com.community.utils.CommonUtil;
import com.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/post")
public class DiscussPostController {

    private final DiscussPostService discussPostService;
    private final HostHolder hostHolder;
    private final UserService userService;

    public DiscussPostController(DiscussPostService discussPostService, HostHolder hostHolder, UserService userService) {
        this.discussPostService = discussPostService;
        this.hostHolder = hostHolder;
        this.userService = userService;
    }


    /**
     * 发布帖子
     *
     * @param title   标题
     * @param content 正文
     */
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

    /**
     * 查询帖子详情
     */
    @GetMapping("/detail/{id}")
    public String getDiscussPost(@PathVariable int id, Model model) {
        DiscussPost post = discussPostService.selectDiscussPostById(id);
        model.addAttribute("post", post);
        User user = userService.selectById(post.getUserId());
        model.addAttribute("user", user);
        return "site/discuss-detail";
    }
}


