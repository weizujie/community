package com.community.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.community.annotation.LoginRequired;
import com.community.entity.User;
import com.community.service.FollowService;
import com.community.service.LikeService;
import com.community.service.UserService;
import com.community.utils.Constant;
import com.community.utils.HostHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Value("${aliyun.oss.file.endpoint}")
    private String endPoint;

    @Value("${aliyun.oss.file.keyid}")
    private String keyId;

    @Value("${aliyun.oss.file.keysecret}")
    private String keySecret;

    @Value("${aliyun.oss.file.bucketname}")
    private String bucketName;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    /**
     * 跳转到用户个人页面
     */
    @LoginRequired
    @GetMapping("/profile/{userId}")
    public String toProfile(@PathVariable int userId, Model model) {
        User user = userService.selectById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        // 用户
        model.addAttribute("user", user);
        // 点赞数量
        int likeCount = likeService.selectUserLikeCount(userId);
        model.addAttribute("likeCount", likeCount);
        // 关注数量
        long followeeCount = followService.selectFolloweeCount(userId, Constant.ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);
        // 粉丝数量
        long followerCount = followService.selectFollowerCount(Constant.ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        // 当前用户是否已关注当前页面上的用户
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), Constant.ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);
        return "site/profile";
    }

    /**
     * 跳转到用户设置页面
     */
    @LoginRequired
    @GetMapping("/setting")
    public String toSetting() {
        return "site/setting";
    }

    /**
     * 更新头像
     */
    @LoginRequired
    @PostMapping("/upload")
    public String uploadAvatar(MultipartFile headerUrl, Model model) {

        if (headerUrl == null) {
            model.addAttribute("error", "请选择图片!");
            return "site/setting";
        }

        String filename = headerUrl.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));

        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件格式不正确!");
            return "site/setting";
        }

        try {
            // 创建 oss 实例
            OSS ossClient = new OSSClientBuilder().build(endPoint, keyId, keySecret);
            // 上传文件输入流
            InputStream inputStream = headerUrl.getInputStream();
            // 获取文件的名称
            String fileName = headerUrl.getOriginalFilename();
            // 在文件名称添加随机的唯一的值
            String uuid = UUID.randomUUID().toString().replace("-", "");
            fileName = uuid + "_" + fileName;
            // 把文件按照日期分类   2020/12/28/xxx.jpg
            String datePath = new DateTime().toString("yyyy/MM/dd");
            fileName = datePath + "/" + fileName;
            // 调用 oss 方法实现文件上传
            // 第一个参数：bucket 名称; 第二个参数：上传到 oss 的路径和名称；第三个参数：上传文件的输入流
            ossClient.putObject(bucketName, fileName, inputStream);
            // 更新当前用户头像的访问路径
            User user = hostHolder.getUser();
            // http://localhost:8080/user/xx.jpg
            String url = "https://" + bucketName + "." + endPoint + "/" + fileName;
            userService.updateHeaderUrl(user.getId(), url);
            // 关闭 ossClient
            ossClient.shutdown();
            return "redirect:/index";
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException("文件上传失败，服务器发生异常!");
        }
    }

    /**
     * 密码修改
     */
    @LoginRequired
    @PostMapping("/password")
    public String changePassword(Model model, String oldPassword, String newPassword, String confirmPassword, @CookieValue String ticket) {
        // 当前登录用户
        User curUser = hostHolder.getUser();
        Map<String, Object> map = userService.changePassword(curUser.getId(), oldPassword, newPassword, confirmPassword);
        // map 为空则修改成功
        if (map.isEmpty()) {
            userService.logout(ticket);
            return "redirect:/login";
        }
        model.addAttribute("OldPasswordMessage", map.get("OldPasswordMessage"));
        model.addAttribute("NewPasswordMessage", map.get("NewPasswordMessage"));
        model.addAttribute("ConfirmPasswordMessage", map.get("ConfirmPasswordMessage"));
        return "site/setting";
    }

}
