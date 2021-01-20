package com.community.controller;

import com.community.entity.Comment;
import com.community.entity.DiscussPost;
import com.community.entity.Page;
import com.community.entity.User;
import com.community.service.CommentService;
import com.community.service.DiscussPostService;
import com.community.service.UserService;
import com.community.utils.CommonUtil;
import com.community.utils.Constant;
import com.community.utils.HostHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/post")
public class DiscussPostController {

    private final DiscussPostService discussPostService;
    private final HostHolder hostHolder;
    private final UserService userService;
    private final CommentService commentService;

    public DiscussPostController(DiscussPostService discussPostService, HostHolder hostHolder, UserService userService, CommentService commentService) {
        this.discussPostService = discussPostService;
        this.hostHolder = hostHolder;
        this.userService = userService;
        this.commentService = commentService;
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
    public String getDiscussPost(@PathVariable int id, Model model, Page page) {
        // 帖子
        DiscussPost post = discussPostService.selectDiscussPostById(id);
        model.addAttribute("post", post);
        // 作者
        User user = userService.selectById(post.getUserId());
        model.addAttribute("user", user);
        // 评论（分页）
        page.setLimit(5);
        page.setPath("/post/detail/" + id);
        page.setRows(post.getCommentCount());

        // 评论列表
        List<Comment> commentList = commentService.selectCommentByEntity(Constant.ENTITY_TYPE_POST, post.getId(), page.getOffset(), page.getLimit());
        // 评论Vo列表
        List<Map<String, Object>> commitVoList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                // 评论Vo
                Map<String, Object> commentVo = new HashMap<>();
                // 评论
                commentVo.put("comment", comment);
                // 作者
                commentVo.put("user", userService.selectById(comment.getUserId()));

                // 回复列表（不作分页）
                List<Comment> replyList = commentService.selectCommentByEntity(Constant.ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                // 回复Vo列表
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        // 回复Vo
                        Map<String, Object> replyVo = new HashMap<>();
                        // 回复
                        replyVo.put("reply", reply);
                        // 作者
                        replyVo.put("user", userService.selectById(reply.getUserId()));
                        // 回复的目标
                        User targetUser = reply.getTargetId() == 0 ? null : userService.selectById(reply.getTargetId());
                        replyVo.put("target", targetUser);

                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);
                // 回复数量
                int replyCount = commentService.selectCountByEntity(Constant.ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);

                commitVoList.add(commentVo);
            }
        }
        model.addAttribute("comments", commitVoList);
        return "site/discuss-detail";
    }
}


