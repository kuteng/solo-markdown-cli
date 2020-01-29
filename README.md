# solo-markdown-cli
上传markdown文档的客户端

## 脚本备忘
- 日常调试： `./pmvn -l spring-boot:run` 。

## TODO
- Monitor中增加逻辑： **如果上一个监控没有执行完，当前监控不触发** 。
  - 或者，当同时运行的监听到达一定数量，就不触发新监听了。