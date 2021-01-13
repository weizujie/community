package com.community.service.impl;

import com.community.entity.User;
import com.community.mapper.UserMapper;
import com.community.service.UserService;
import com.community.utils.CommunityConstant;
import com.community.utils.CommunityUtil;
import com.community.utils.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService, CommunityConstant {

    private final UserMapper userMapper;
    private final MailClient mailClient;
    private final TemplateEngine templateEngine;

    public UserServiceImpl(UserMapper userMapper, MailClient mailClient, TemplateEngine templateEngine) {
        this.userMapper = userMapper;
        this.mailClient = mailClient;
        this.templateEngine = templateEngine;
    }

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * 根据用户 id 查询用户
     */
    @Override
    public User selectById(int id) {
        return userMapper.selectById(id);
    }


    /**
     * 根据用户名查询用户
     */
    @Override
    public User selectByUsername(String username) {
        return selectByUsername(username);
    }

    /**
     * 根据邮箱查询用户
     */
    @Override
    public User selectByEmail(String email) {
        return selectByEmail(email);
    }

    /**
     * 添加用户
     */
    @Override
    public void insertUser(User user) {
        userMapper.insertUser(user);
    }


    /**
     * 修改用户状态
     */
    @Override
    public void updateStatus(int userId, int status) {
        userMapper.updateStatus(userId, status);
    }

    /**
     * 注册用户
     */
    @Override
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();
        // 空值判断
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("UsernameMessage", "账号不能为空!");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("PasswordMessage", "密码不能为空!");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("EmailMessage", "邮箱不能为空!");
            return map;
        }
        // 验证账号
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if (dbUser != null) {
            map.put("UsernameMessage", "该账号已存在!");
            return map;
        }
        // 验证邮箱
        dbUser = userMapper.selectByEmail(user.getEmail());
        if (dbUser != null) {
            map.put("EmailMessage", "该邮箱已被注册!");
            return map;
        }
        // 注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 发送激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // http://localhost:8080/activation/user_id/code
        String url = domain + contextPath + "activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "Community-激活账号", content);

        return map;
    }

    /**
     * 激活用户
     */
    @Override
    public int activation(int userId, String code) {
        User dbUser = userMapper.selectById(userId);
        if (dbUser.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if (dbUser.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;
        }
    }
}
