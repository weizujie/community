package com.community;

import com.community.utils.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
public class MailTest {

    @Autowired
    private MailClient client;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void test() {
        client.sendMail("byojiaoxianz7@outlook.com", "TEST", "Welcome!");
    }


    @Test
    public void test1() {
        Context context = new Context();
        context.setVariable("username", "sunday");
        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);

        client.sendMail("byojiaoxianz7@outlook.com", "TEST", content);

    }
}
