## 简介

该项目属于开源SAAS化项目，主要业务涵盖医院医生管理、医院排班以及患者关注“助健康导航服务”公众号。患者可通过扫描医院二维码实时排队，医生则可在 PC 端进行就诊和患者信息管理。

1. **便捷性和效率**：通过扫描医院二维码进行排队，省去了传统挂号的繁琐流程，提高了患者就医的便捷性。医生在 PC 端开始就诊和管理患者信息，提高了医疗服务的效率。
2. **智能化服务**：患者关注“助健康导航服务”公众号后，可以享受到智能化的健康服务，例如预约挂号、健康咨询等，为患者提供更加个性化的医疗体验。
3. **数据整合和管理**：项目将患者信息、排队信息、医生管理等整合在一起，有利于医院对数据进行统一管理和分析，提高了医院运营的效率和质量。
4. **用户体验优化**：项目通过简化挂号排队流程和提供智能化服务，优化了用户体验，提升了患者对医疗服务的满意度，有助于提升医院的口碑和竞争力。

## 一、后端

- 基础框架：Spring Boot 2.6.6

- 微服务框架： Spring Cloud Alibaba 2021.0.1.0

- 持久层框架：MybatisPlus 3.5.1

- 报表工具： JimuReport 1.5.2

- 安全框架：Apache Shiro 1.8.0，Jwt 3.11.0

- 消息中间件：disruptor

- 数据库：MySQL

- 数据库连接池：阿里巴巴Druid 1.1.22

- 日志打印：logback

- 其他：autopoi, fastjson，poi，Swagger-ui，quartz, lombok（简化代码）等。

  

  **基础框架教程地址：**

  https://help.jeecg.com/java/readme.html

## 二、PC端系统相关页面

演示地址：http://36.41.65.31:8082/login  admin/123456  (请勿删除数据)


1、注册：

![image](https://github.com/juyinh/jykj-hospital-saas/assets/164468654/380b1614-24d6-400e-a86d-14182bd55a7d)

租户审核：



2、系统首页

![image](https://github.com/juyinh/jykj-hospital-saas/assets/164468654/e38811c3-3694-426c-8add-d9fda0f0ddf2)

3、基本设置

3.1科室设置

![image](https://github.com/juyinh/jykj-hospital-saas/assets/164468654/40bc14ef-d76b-426a-8435-ebb9eeb216a3)

3.2诊室设置

![image](https://github.com/juyinh/jykj-hospital-saas/assets/164468654/d413a211-480f-40a8-aaa1-2d8fd706eaf8)

4、角色设置

![image](https://github.com/juyinh/jykj-hospital-saas/assets/164468654/8c4511aa-17cb-4ce6-87ae-c20efb679fbd)

5、人员管理

![image](https://github.com/juyinh/jykj-hospital-saas/assets/164468654/affe99a1-910a-4090-9f6a-1e98dd29372b)

6、排班设置

![image](https://github.com/juyinh/jykj-hospital-saas/assets/164468654/944b153d-05d0-4088-9bc2-a6ff9112e216)

7、医生就诊页面

![image](https://github.com/juyinh/jykj-hospital-saas/assets/164468654/8b992925-8a3a-479d-bee4-f069df35cffa)

## 三、微信公众号端

正式环境公众号：
![qrcode_for_gh_3c4ced95e1f3_430](https://github.com/juyinh/jykj-hospital-saas/assets/164468654/26a35826-ce77-40cb-8f31-887bb00b8449)

演示公众号
![qrcode_for_gh_36c05e0b73cc_430](https://github.com/juyinh/jykj-hospital-saas/assets/164468654/fb99b0c6-5146-4244-8378-d48ee15e2c46)



1、首页

![image](https://github.com/juyinh/jykj-hospital-saas/assets/164468654/d60c76ff-eb81-4c5b-bf14-022add0d6801)

2、扫码排队

![image](https://github.com/juyinh/jykj-hospital-saas/assets/164468654/fac65f10-3fdc-48ec-a58a-70d575f5155c)

2.1排队成功

![image](https://github.com/juyinh/jykj-hospital-saas/assets/164468654/976c5265-83af-4dd6-a39a-2bb63a8d3662)


3、排队列表

![image](https://github.com/juyinh/jykj-hospital-saas/assets/164468654/4f75729c-5cb6-4025-9db7-605f21a47773)







