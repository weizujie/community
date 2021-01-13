package com.community;

import com.community.entity.DiscussPost;
import com.community.entity.User;
import com.community.mapper.DiscussPostMapper;
import com.community.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MapperTest {

    @Autowired
    private DiscussPostMapper discussPostMapper;


    @Autowired
    private UserMapper userMapper;

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
    public void UserTest() {
        User user = userMapper.selectUserById(11);
        System.out.println(user.toString());
    }

}
