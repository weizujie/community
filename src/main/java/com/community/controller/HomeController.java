package com.community.controller;


import com.community.entity.DiscussPost;
import com.community.entity.Page;
import com.community.entity.User;
import com.community.service.DiscussPostService;
import com.community.service.LikeService;
import com.community.service.UserService;
import com.community.utils.CommonUtil;
import com.community.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private LikeService likeService;

    /**
     * 社区首页，展示贴子列表
     * 方法调用前，SpringMVC 会自动实例化 Model 和 Page，并将 Page 注入 Model，
     * 所以，在 thymeleaf 中可以直接访问 Page 对象中的数据，不需要再 model.addAttribute() 方法。
     * -------
     * userId 为 0 表示： select * from user
     * userId 不为 0 表示：select * from user where user_id = #{userId}
     * 该做法是为了在用户个人主页上可以查询到某个用户发布的帖子
     */
    @GetMapping({"/index", "/"})
    public String index(Model model, Page page) {
        page.setRows(discussPostService.selectDiscussPostRows(0));
        page.setPath("/index");

        // 查询所有帖子
        List<DiscussPost> discussPosts = discussPostService.selectDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> list = new ArrayList<>();
        for (DiscussPost post : discussPosts) {
            Map<String, Object> map = new HashMap<>();
            map.put("post", post);
            User user = userService.selectById(post.getUserId());
            map.put("user", user);

            // 点赞数量
            long likeCount = likeService.selectEntityLikeCount(Constant.ENTITY_TYPE_POST, post.getId());
            map.put("likeCount", likeCount);

            list.add(map);
        }
        model.addAttribute("discussPosts", list);
        return "index";

    }

    @GetMapping("/error")
    public String toErrorPage() {
        return "error/500";
    }
}
