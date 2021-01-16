package com.community;

import com.community.entity.DiscussPost;
import com.community.entity.LoginTicket;
import com.community.entity.User;
import com.community.mapper.DiscussPostMapper;
import com.community.mapper.LoginTicketMapper;
import com.community.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class MapperTest {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    public void discussPostTest() {
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(101, 1, 10);
        for (DiscussPost post : discussPosts) {
            System.out.println(post);
        }

        int rows = discussPostMapper.selectDiscussPostRows(101);
        System.out.println(rows);
    }

    @Test
    public void userTest() {
        User user = userMapper.selectById(11);
        System.out.println(user.toString());
    }

    @Test
    public void loginTicketTest() {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(153);
        loginTicket.setStatus(0);
        loginTicket.setTicket("awdawdawd");
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));

        loginTicketMapper.updateStatus("awdawdawd", 1);
    }
}
