package com.community.controller;

import com.community.annotation.LoginRequired;
import com.community.entity.Comment;
import com.community.entity.DiscussPost;
import com.community.entity.Page;
import com.community.entity.User;
import com.community.service.CommentService;
import com.community.service.DiscussPostService;
import com.community.service.LikeService;
import com.community.service.UserService;
import com.community.utils.Constant;
import com.community.utils.UserThreadLocal;
import com.community.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/post")
public class DiscussPostController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserThreadLocal userThreadLocal;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    /**
     * 发布帖子
     *
     * @param title   标题
     * @param content 正文
     */
    @PostMapping("/add")
    @ResponseBody
    @LoginRequired
    public String addDiscussPost(String title, String content) {
        // 判断用户是否登录
        User loginUser = userThreadLocal.getUser();
        if (loginUser == null) {
            return ResultVo.getJsonString(-1, "请登录后再操作!");
        }

        // 发布帖子
        DiscussPost post = new DiscussPost();
        post.setUserId(loginUser.getId());
        post.setTitle(title);
        post.setStatus(0);
        post.setType(0);
        post.setScore(0.0);
        post.setCommentCount(0);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.insertDiscussPost(post);
        return ResultVo.getJsonString(0, "发布成功!");
    }

    /**
     * 查询帖子详情
     * 帖子的回复分为评论：1.对帖子进行评论（comment） 2.对评论进行评论（reply）
     * 首先展示所有的 comment，再针对每个 comment 展示 reply
     */
    @GetMapping("/detail/{id}")
    public String getDiscussPost(@PathVariable int id, Model model, Page page) {
        // 帖子
        DiscussPost post = discussPostService.selectDiscussPostById(id);
        model.addAttribute("post", post);

        // 用户
        User user = userService.selectById(post.getUserId());
        model.addAttribute("user", user);

        // 点赞数量
        long likeCount = likeService.selectEntityLikeCount(Constant.ENTITY_TYPE_POST, id);
        model.addAttribute("likeCount", likeCount);

        // 点赞状态
        int likeStatus = userThreadLocal.getUser() == null ? 0 : likeService.selectEntityLikeStatus(userThreadLocal.getUser().getId(), Constant.ENTITY_TYPE_POST, id);
        model.addAttribute("likeStatus", likeStatus);

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

                // 用户
                commentVo.put("user", userService.selectById(comment.getUserId()));

                // 点赞数量
                likeCount = likeService.selectEntityLikeCount(Constant.ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeCount", likeCount);

                // 点赞状态
                likeStatus = userThreadLocal.getUser() == null ? 0 : likeService.selectEntityLikeStatus(userThreadLocal.getUser().getId(), Constant.ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeStatus", likeStatus);

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

                        // 用户
                        replyVo.put("user", userService.selectById(reply.getUserId()));

                        // 回复的目标
                        User targetUser = reply.getTargetId() == 0 ? null : userService.selectById(reply.getTargetId());
                        replyVo.put("target", targetUser);

                        // 点赞数量
                        likeCount = likeService.selectEntityLikeCount(Constant.ENTITY_TYPE_COMMENT, reply.getId());
                        replyVo.put("likeCount", likeCount);

                        // 点赞状态
                        likeStatus = userThreadLocal.getUser() == null ? 0 : likeService.selectEntityLikeStatus(userThreadLocal.getUser().getId(), Constant.ENTITY_TYPE_COMMENT, reply.getId());
                        replyVo.put("likeStatus", likeStatus);

                        replyVoList.add(replyVo);
                    }
                }

                commentVo.put("replys", replyVoList);

                // 回复数量
                Long replyCount = commentService.selectCount(Constant.ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);

                commitVoList.add(commentVo);
            }
        }

        model.addAttribute("comments", commitVoList);
        return "site/discuss-detail";
    }
}


