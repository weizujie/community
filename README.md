# Community 

> 基于 SpringBoot + Mybatis + Redis 开发的问答类社区。 


## 项目演示

- 演示地址：http://42.192.146.160:8080
- 账号密码：
  - admin/admin 
  - test/test


## 运行效果

### 首页
![首页](https://weizujie.oss-cn-shenzhen.aliyuncs.com/img/image-20210122180027532.png)

### 帖子评论/回复

![帖子评论/回复](https://weizujie.oss-cn-shenzhen.aliyuncs.com/img/image-20210122180557769.png)

### 个人主页

![个人主页](https://weizujie.oss-cn-shenzhen.aliyuncs.com/img/image-20210122180108327.png)

### 我的帖子

![我的帖子](https://weizujie.oss-cn-shenzhen.aliyuncs.com/img/image-20210128143205727.png)

### 发送私信且未读

![发送私信且未读](https://weizujie.oss-cn-shenzhen.aliyuncs.com/img/image-20210122180750435.png)

### 私信列表

![私信列表](https://weizujie.oss-cn-shenzhen.aliyuncs.com/img/image-20210122180429898.png)

### 关注列表

![关注列表](https://weizujie.oss-cn-shenzhen.aliyuncs.com/img/image-20210123100146107.png)

### 粉丝列表

![粉丝列表](https://weizujie.oss-cn-shenzhen.aliyuncs.com/img/image-20210123100114699.png)

## 知识整理

### Interceptor 的作用?在项目中哪些地方用到?

SpringMVC 中的 Interceptor 拦截器的主要作用是**拦截用户的请求并进行相应的处理**，比如判断用户是否登录。

SpringMVC 中的 Interceptor 拦截请求是通过 HandlerInterceptor 来实现的。HandlerInterceptor  中实现了三个方法：

- preHandle()：在 Controller 中方法调用之前执行，若返回值为 true，则继续执行下一个 handle，否则停止执行

- postHandle()：在 Controller 中方法调用之后，DispatcherServlet 进行视图的渲染之前执行（前提是 preHandle() 返回 true）

- afterCompletion()：该方法将在整个请求完成之后，也就是DispatcherServlet渲染了视图执行（前提是 preHandle() 返回 true）

>  该项目中，每次请求都会检查 request 中的 login_ticket，把找到的 user 信息存在 ThreadLocal 中，在完成请求的处理后自动释放。