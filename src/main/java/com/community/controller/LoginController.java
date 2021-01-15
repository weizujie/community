package com.community.controller;


import com.community.entity.User;
import com.community.service.UserService;
import com.community.utils.CommunityConstant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }


    /**
     * 跳转到用户登录页面
     */
    @GetMapping("/login")
    public String toLogin() {
        return "site/login";
    }

    /**
     * 跳转到用户注册界面
     */
    @GetMapping("/register")
    public String toRegister() {
        return "site/register";
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public String register(Model model, User user) {
        Map<String, Object> map = userService.register(user);
        // map 为空则注册成功
        if (map == null || map.isEmpty()) {
            model.addAttribute("msg", "注册成功，请到邮箱激活该账号!");
            model.addAttribute("target", "/index");
            return "site/operate-result";
        } else {
            model.addAttribute("UsernameMessage", map.get("UsernameMessage"));
            model.addAttribute("PasswordMessage", map.get("PasswordMessage"));
            model.addAttribute("EmailMessage", map.get("EmailMessage"));
            return "site/register";
        }
    }

    /**
     * 邮箱激活账号
     */
    @GetMapping("/activation/{userId}/{code}")
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code) {
        // 根据用户 id 查询该用户的状态
        int result = userService.activation(userId, code);
        if (result == ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "激活成功，你的账号可以正常使用!");
            model.addAttribute("target", "/login");
        } else if (result == ACTIVATION_REPEAT) {
            model.addAttribute("msg", "无效操作，该账号已激活过了!");
            model.addAttribute("target", "/index");
        } else {
            model.addAttribute("msg", "激活失败，该账号激活码无效!");
            model.addAttribute("target", "/index");
        }
        return "site/operate-result";
    }

}
