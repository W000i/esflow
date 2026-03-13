# esflow
<<<<<<< HEAD
esflow是一款轻量、快速的国产开源工作流平台，可作为工作流引擎、在线办公系统基础平台使用，支持中国式流程办理方式，如退回、跳转、驳回、前加签、后加签等。采用json存储流程设计数据，数据库结构简单，总共只有8张表。通过拖拽即可完成工作流设计，全程无需写一行代码即可搭建企业级业务流程平台。且支持中国式流程办理方式，如退回、跳转、驳回、前加签、后加签等。此仓库为esflow-ui后端api代码仓库
=======

#### 1. 介绍
esflow是一款轻量、快速的国产开源工作流平台，可作为工作流引擎、在线办公系统基础平台使用，支持中国式流程办理方式，如退回、跳转、驳回、前加签、后加签等。采用json存储流程设计数据，数据库结构简单，总共只有8张表。通过拖拽即可完成工作流设计，全程无需写一行代码即可搭建企业级业务流程平台。且支持中国式流程办理方式，如退回、跳转、驳回、前加签、后加签等。此仓库为esflow-ui后端api代码仓库。

演示地址：http://120.79.67.190:8002/#/login 用户名：admin 密码： Super@2023

#### 2. 软件架构
- **后端：采用JDK17、Spring boot 2.7+、Mybatis Plus、SaToken(权限验证框架)、AnyLine（表管理）。<br/>** 
- **前端：采用Vite6.0+ 、Vue3.5+、ElementPlus2.9+、Typescript5.7+、Pure Admin。<br/>** 


#### 3.安装教程

1.  git clone 
2.  创建MYSQL数据库
CREATE DATABASE esflow DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
3.  修改pom.xml中<lib.path>F:\stsworkspace-esflow\esflow</lib.path>为lib文件夹绝对路径。
- **订阅pro商业版后，将提供上述工程源代码。<br/>** 
4.  编译<br/>
maven clean compile<br/>
maven install<br/>
5.  修改数据库配置
- 修改esflow-base-api/pom.xml中mysql连接地址、用户名、密码、端口
- 修改esflow-base-api/src/main/resources/application-dev.yml中mysql连接地址、用户名、密码、端口	
6.  启动项目<br/>
启动后系统将通过flyway将classpath://db/migration下的V1.0.0__init_freedb.sql脚本初始化到之前新建的esflow数据库。

#### 4. 功能展示
1.  ![登录](https://foruda.gitee.com/images/1740629849599202201/00a0cda9_8293053.png "login.png")
2.  ![首页](https://foruda.gitee.com/images/1740629896432249530/18bb51bc_8293053.png "welcome.png")
3.  ![表定义](https://foruda.gitee.com/images/1740629923126451115/8813dd7f_8293053.png "deftable.png")
4.  ![表字段](https://foruda.gitee.com/images/1740629956865000747/f4d4498f_8293053.png "deftable-edit.png")
5.  ![表单](https://foruda.gitee.com/images/1740629996764394683/1a51c163_8293053.png "form.png")
6.  ![表单编辑](https://foruda.gitee.com/images/1740630020167267721/586fda62_8293053.png "form-base.png")
7.  ![表单设计](https://foruda.gitee.com/images/1740630042331565484/c378f857_8293053.png "form-design.png")
8.  ![表单预览](https://foruda.gitee.com/images/1740630067175284000/f394cce0_8293053.png "form-preview.png")
9.  ![表单预览](https://foruda.gitee.com/images/1740630090666264777/01db77f3_8293053.png "form-preview-assetsapply.png")
10. ![流程定义](https://foruda.gitee.com/images/1740630116562127132/c723398c_8293053.png "defflow.png")
11. ![流程编辑](https://foruda.gitee.com/images/1740630142350757678/bf147906_8293053.png "deflow-base.png")
12. ![流程设计](https://foruda.gitee.com/images/1740630166668682805/550a40b2_8293053.png "defflow-design.png")
13. ![工单管理](https://foruda.gitee.com/images/1740630198144102045/25be2ecd_8293053.png "order.png")
14. ![工单申请](https://foruda.gitee.com/images/1740630219371901191/9089b23a_8293053.png "order-apply.png")
15. ![已办任务](https://foruda.gitee.com/images/1740630255260131095/c26c7422_8293053.png "task-done.png")
16. ![审批过程](https://foruda.gitee.com/images/1740630284011172413/7fa9722c_8293053.png "task-done-comment.png")
17. ![流程图跟踪](https://foruda.gitee.com/images/1740630309175751247/23216be5_8293053.png "task-done-photo.png")
18. ![已办任务工单](https://foruda.gitee.com/images/1740630340323229773/ece11fa9_8293053.png "task-done-view.png")
19. ![待办任务](https://foruda.gitee.com/images/1740630366490201321/626bc2dc_8293053.png "task-todo.png")
20. ![催办](https://foruda.gitee.com/images/1740630598684810256/b83f1593_8293053.png "task-todo-progress.png")

#### 5. 开源版与pro版区别

##### 功能区别
   ![微信号](https://foruda.gitee.com/images/1741921808360786331/604cc415_8293053.png)
   
##### 源码区别
   ![微信号](https://foruda.gitee.com/images/1741921842504073350/78c4db06_8293053.png)

#### 6. 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request

#### 7. 联系作者

![微信号](https://foruda.gitee.com/images/1740631334627973395/6005e9d2_8293053.jpeg?width=300 "vv.jpg")
![QQ群](https://foruda.gitee.com/images/1741767349017773118/6b4b1cb7_8293053.jpeg?width=300 "qqvip.jpg")

#### 8. 版权说明

1.  如需使用请保留版权说明。
2.  本软件已申请软件著作权，如在使用时未保留版权说明，属于违法盗用，我们将保留追究法律责任的权利。
3.  EsFlow Pro版本将提供配套的后台管理代码。如有意向请添加微信或qq交流群咨询。
>>>>>>> f441096 (first commit)
