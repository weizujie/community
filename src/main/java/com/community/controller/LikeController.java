package com.community.controller;

import com.community.entity.User;
import com.community.service.LikeService;
import com.community.utils.HostHolder;
import com.community.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @PostMapping("/like")
    @ResponseBody
    public String like(int entityType, int entityId, int entityUserId) {
        User curUser = hostHolder.getUser();
        // 点赞
        likeService.like(curUser.getId(), entityType, entityId, entityUserId);
        // 数量
        long likeCount = likeService.selectEntityLikeCount(entityType, entityId);
        // 状态
        int likeStatus = likeService.selectEntityLikeStatus(curUser.getId(), entityType, entityId);
        // 返回结果
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);

        return ResultVo.getJsonString(0, null, map);

    }

}
