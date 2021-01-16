package com.community.controller;


import com.community.entity.DiscussPost;
import com.community.entity.Page;
import com.community.entity.User;
import com.community.service.DiscussPostService;
import com.community.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页 前端控制器
 */
@Controller
public class HomeController {

    private final UserService userService;
    private final DiscussPostService discussPostService;

    public HomeController(UserService userService, DiscussPostService discussPostService) {
        this.userService = userService;
        this.discussPostService = discussPostService;
    }

    /**
     * 社区首页，展示贴子列表
     * 方法调用前，SpringMVC 会自动实例化 Model 和 Page，并将 Page 注入 Model，
     * 所以，在 thymeleaf 中可以直接访问 Page 对象中的数据，不需要再 model.addAttribute() 方法。
     */
    @GetMapping({"/index", "/"})
    public String index(Model model, Page page) {
        page.setRows(discussPostService.selectDiscussPostRows(0));
        page.setPath("/index");

        List<DiscussPost> discussPosts = discussPostService.selectDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> list = new ArrayList<>();
        for (DiscussPost post : discussPosts) {
            Map<String, Object> map = new HashMap<>();
            map.put("post", post);
            User user = userService.selectById(post.getUserId());
            map.put("user", user);
            list.add(map);
        }
        model.addAttribute("discussPosts", list);
        return "index";

    }
}
