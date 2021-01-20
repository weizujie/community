package com.community.controller;

import com.community.annotation.LoginRequired;
import com.community.entity.User;
import com.community.service.UserService;
import com.community.utils.CommonUtil;
import com.community.utils.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;


    /**
     * 跳转到用户个人页面
     */
    @LoginRequired
    @GetMapping("/profile")
    public String toProfile() {
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

        // 生成随机文件名
        filename = CommonUtil.generateUUID() + suffix;
        // 确定文件存放的路径
        File dest = new File(uploadPath + "/" + filename);
        System.out.println(dest);
        try {
            headerUrl.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件上传失败，服务器发生异常!");
        }

        // 更新当前用户头像的访问路径
        User user = hostHolder.getUser();
        // http://localhost:8080/user/xx.jpg
        String url = domain + contextPath + "user/upload/" + filename;
        userService.updateHeaderUrl(user.getId(), url);
        return "redirect:/index";
    }

    /**
     * 展示头像
     */
    @GetMapping("/upload/{filename}")
    public void showAvatar(@PathVariable String filename, HttpServletResponse response) {
        // 服务器存放路径
        filename = uploadPath + "/" + filename;
        // 文件后缀
        String suffix = filename.substring(filename.lastIndexOf("."));
        // 响应图片
        response.setContentType("image/" + suffix);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            FileInputStream fileInputStream = new FileInputStream(filename);
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, b);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
