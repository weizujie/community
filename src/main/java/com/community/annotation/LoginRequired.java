package com.community.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用来描述哪些方法被拦截器拦截（需要配置拦截器）
 * Target(ElementType.METHOD) -> 用来描述方法
 * Retention(RetentionPolicy.RUNTIME) -> 声明该注解有效的时长（程序运行的时候有效）
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {
}
