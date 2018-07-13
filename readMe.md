
1、项目清单

| 项目名称      |  启动顺序  |  用途 |
| --------   | -----:   | :----: |
| springcloud-client     |    3   |   angularjs   |
| springcloud-zuul      |   4 |   网关   |
| springcloud-oauth2-auth-server        |  1   |   认证服务器    |
| springcloud-resource-server       |  2 |   资源服务器    |
| springcloud-client      |  5 |   单点登录测试客户端   |


2、需要在数据库中创建名为alan-oauth2的数据库 账号密码root/root


3、访问路径
	
	单点登录测试-》http://127.0.0.1:7777/ 点击TJ登录
	账号输入->admin admin
	登陆成功->首页页面点击系统B即可单点至B系统
