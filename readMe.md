
1、项目清单

| 项目名称      |  启动顺序  |  用途 |
| --------   | -----:   | :----: |
| springcloud-client     |    3   |   angularjs ,zuul代理  |
| springcloud-zuul      |   4 |   网关   |
| springcloud-oauth2-auth-server        |  1   |   认证服务器    |
| springcloud-resource-server       |  2 |   资源服务器    |
| springcloud-oauth2-client     |  5 |   单点登录测试客户端   |


2、需要在数据库中创建名为alan-oauth2的数据库 账号密码root/root


3、访问路径
	
	单点登录测试-》http://127.0.0.1:7777/ 点击TJ登录
	账号输入->admin admin
	登陆成功->首页页面点击系统B即可单点至B系统
4、当工程启动不了的时候，可能是上传的resouces的文件夹不是源文件夹了，需要手动变更一下。怎么控制上传文件夹的类型我忘记了好像是文件中该什么，如果有知道的同学给我留言多谢
