# solo-markdown-cli
上传markdown文档的客户端

## 备忘
- java版本 1.8
- 如果是新环境，需要将 `src/main/java/resource/applicaton.yml` 中的配置 `spring.datasource.initialization-mode` 改为 `always` 以便创建数据库。如果已有数据库，则将该配置改为 `never` 。

## 脚本备忘
- 日常调试： `./pmvn -l spring-boot:run` 。

## 用法
- 触发文章更新：`GET http://localhost:8810/input`
- 全部文章更新: `GET http://localhost:8810/input-all`

## TODO
- Monitor中增加逻辑： **如果上一个监控没有执行完，当前监控不触发** 。
  - 或者，当同时运行的监听到达一定数量，就不触发新监听了。
  
## 建议
- `FileAlterationObserver` 应该提供刷新 `rootEntry` 的接口，这样在修改文件之后，可以避免重新操作。
- `FileAlterationObserver` 应该提供更新 `rootEntry` 的接口，这样在以 *脚本方式* 启动文件监听时，可以将上次监听的结果（文件形式保存在硬盘上）读取进来，并对比。