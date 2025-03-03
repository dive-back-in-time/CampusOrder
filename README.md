# lsm-takeout
## 介绍
基于Spring Boot + MyBatis + Redis + MySQL 的外卖点餐平台

## 项目工具：  
SpringBoot、Mybatis、MySQL、Redis、Nginx 、Postman
## 项目描述：  
Campus点餐是一个面向校园的餐饮服务平台，项目采用模块化设计，面向管理员的后台管理系统集成员工管理、菜品管理等功能，前端小程序集成了浏览菜品、添加购物车等功能。
## 工作内容：  
- 基于Filter过滤器实现了Api访问日志系统，自动记录 API 调用详细信息，助于运维排查与故障监控
- 基于SpringMVC框架搭建项目，使用 Redis 作为缓存并集成 Spring Cache ，降低管理缓存的复杂性
- 结合AOP+自定义注解实现公共字段自动填充，降低业务代码耦合度
- 基于websocket实现客户端来单提醒功能，响应时间从秒级降低至毫秒级，减少轮询的性能消耗
![Uploading image.png…]()

