package com.community.interceptor;

import com.community.annotation.LoginRequired;
import com.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 用户登录拦截器
 * 使用自定义注解，作用于方法上，哪个方法有该注解就拦截哪个方法
 * 作用：如果用户没有登录就访问如 /user/profile 等页面就会被拦截，强制跳转到登陆页面
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断拦截的目标是否为方法
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            // 判断用户是否登录
            if (loginRequired != null && hostHolder.getUser() == null) {
                // 如果没有登录，跳转到登陆页面
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }
        }
        return true;
    }
}
