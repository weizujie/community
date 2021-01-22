package com.community.controller;

import com.community.entity.User;
import com.community.service.FollowService;
import com.community.utils.HostHolder;
import com.community.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FollowController {

    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;

    /**
     * 取消关注
     */
    @PostMapping("/follow")
    @ResponseBody
    public String follow(int entityType, int entityId) {
        // TODO:用户未登录时的处理：拦截器拦截
        User curUser = hostHolder.getUser();
        followService.follow(curUser.getId(), entityType, entityId);
        return ResultVo.getJsonString(0, "已关注");
    }

    /**
     * 取消关注
     */
    @PostMapping("/unfollow")
    @ResponseBody
    public String unfollow(int entityType, int entityId) {
        // TODO:用户未登录时的处理：拦截器拦截
        User curUser = hostHolder.getUser();
        followService.unfollow(curUser.getId(), entityType, entityId);
        return ResultVo.getJsonString(0, "已取消关注");
    }

}
