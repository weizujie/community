package com.community.controller;

import com.community.annotation.LoginRequired;
import com.community.entity.Page;
import com.community.entity.User;
import com.community.service.FollowService;
import com.community.service.UserService;
import com.community.utils.Constant;
import com.community.utils.HostHolder;
import com.community.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class FollowController {

    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    /**
     * 取消关注
     */
    @PostMapping("/follow")
    @ResponseBody
    @LoginRequired
    public String follow(int entityType, int entityId) {
        User curUser = hostHolder.getUser();
        followService.follow(curUser.getId(), entityType, entityId);
        return ResultVo.getJsonString(0, "已关注");
    }

    /**
     * 取消关注
     */
    @PostMapping("/unfollow")
    @ResponseBody
    @LoginRequired
    public String unfollow(int entityType, int entityId) {
        User curUser = hostHolder.getUser();
        followService.unfollow(curUser.getId(), entityType, entityId);
        return ResultVo.getJsonString(0, "已取消关注");
    }


    @GetMapping("/followees/{userId}")
    public String getFollowees(@PathVariable int userId, Page page, Model model) {
        User user = userService.selectById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        model.addAttribute("user", user);

        // 分页条件
        page.setLimit(5);
        page.setPath("/followees/" + userId);
        page.setRows((int) followService.selectFolloweeCount(userId, Constant.ENTITY_TYPE_USER));

        List<Map<String, Object>> followeeList = followService.selectFolloweeList(userId, page.getOffset(), page.getLimit());
        if (followeeList != null) {
            for (Map<String, Object> map : followeeList) {
                User u = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users", followeeList);
        return "site/followee";
    }

    @GetMapping("/followers/{userId}")
    public String getFollowers(@PathVariable int userId, Page page, Model model) {
        User user = userService.selectById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        model.addAttribute("user", user);

        // 分页条件
        page.setLimit(5);
        page.setPath("/followers/" + userId);
        page.setRows((int) followService.selectFollowerCount(Constant.ENTITY_TYPE_USER, userId));

        List<Map<String, Object>> followerList = followService.selectFollowerList(userId, page.getOffset(), page.getLimit());
        if (followerList != null) {
            for (Map<String, Object> map : followerList) {
                User u = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users", followerList);
        return "site/follower";
    }

    private boolean hasFollowed(int userId) {
        if (hostHolder.getUser() == null) {
            return false;
        }
        return followService.hasFollowed(hostHolder.getUser().getId(), Constant.ENTITY_TYPE_USER, userId);
    }

}
