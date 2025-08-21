## 1. 全文检索功能实现

全文检索（Full-Text Search）是指对文本内容进行高效检索的能力，不仅可以匹配关键字，还能根据相关度进行排序，支持分词、模糊匹配、同义词匹配等高级搜索功能。在实际开发中，我选择使用 **Elasticsearch（ES）+ Spring Boot + MyBatis Plus** 的方案来实现这一功能。

### 1.1 技术选型与架构

- **搜索引擎**：Elasticsearch 7.x
   优势在于分布式、可扩展、近实时搜索、强大的全文检索和分析能力。
- **分词器**：IK Analyzer
   适用于中文分词，支持细粒度分词和扩展词库。
- **后端框架**：Spring Boot
   提供 RESTful 接口给前端调用，实现与 Elasticsearch 的交互。
- **数据同步**：MySQL → ES
   使用 **Canal + Rabbitmq** 或者直接在数据写入数据库时同步写入 ES。

架构流程：

1. 用户在前端输入搜索关键字。
2. 后端通过 Elasticsearch 发起查询请求。
3. ES 根据索引进行分词匹配，返回结果并按相关度（score）排序。
4. 后端将结果封装为统一响应对象返回给前端。

### 1.2 查询实现

```java
private Object searchQuestion(SearchQuery qo) throws InvocationTargetException, IllegalAccessException {
    Page<QaQuestions> qaQuestions = searchService.searchHighLight(QaQuestions.class, QuestionEs.class, qo, "title", "detailDoubt");
    return qaQuestions;
}
```

```java
@Override
public <T> Page<T> searchHighLight(Class<T> clazz, Class<?> esClazz, SearchQuery qo, String... fields) throws InvocationTargetException, IllegalAccessException {
    NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
    builder.withQuery(
            QueryBuilders.multiMatchQuery(qo.getKeyword(), fields)
    );
    Pageable  pageable = PageRequest.of(qo.getCurrentPage()-1, qo.getPageSize());
    builder.withPageable(pageable);

    List<HighlightBuilder.Field> highlightFields = new ArrayList<>();
    for (String field : fields) {
        HighlightBuilder.Field highlightField = new HighlightBuilder.Field(field)
                .preTags("<span style='color:red'>")
                .postTags("</span>");
        highlightFields.add(highlightField);
    }
    builder.withHighlightFields(highlightFields);

    SearchHits<?> hits = template.search(builder.build(), esClazz);
    long total = hits.getTotalHits();

    //当前页数据
    List<T> content = new ArrayList<>();

    for (SearchHit<?> hit : hits.getSearchHits()) {
        Long id = Long.valueOf(hit.getId());
        T t = null; //到mysql中查询得到的结果放在t中
        if(Strategy.class == clazz){
            t = (T) remoteStrategyService.getOne(id, SecurityConstants.INNER).getData();
        }else if(Note.class == clazz){
            t = (T) remoteNoteService.getOne(id, SecurityConstants.INNER).getData();
        }else if(UserInfo.class == clazz){
            t =  (T) remoteUserInfoService.getOne(id, SecurityConstants.INNER).getData();
        }else if (Destination.class == clazz){
            t = (T) remoteDestinationService.getOne(id, SecurityConstants.INNER).getData();
        } else if (QaQuestions.class == clazz){
            t = (T) remoteQaQuestionsService.getOne(id, SecurityConstants.INNER).getData();
        }

        Map<String, List<String>> map = hit.getHighlightFields();
        for (String key : map.keySet()) {
            List<String> list = map.get(key);
            StringBuffer stringBuffer = new StringBuffer();
            for (String s : list) {
                stringBuffer.append(s + " ");
            }
            String value = stringBuffer.toString();
            BeanUtils.setProperty(t, key, value);
        }
        content.add(t);
    }

    Page<T> page = new PageImpl<T>(content, pageable, total);

    return page;
}
```

### 1.3 数据同步策略

​	**批量同步**：定时任务（Quartz/Spring Scheduler）批量同步数据库数据到 ES。

若依是一套全部开源的快速开发平台，毫无保留给个人及企业免费使用。

* 采用前后端分离的模式，微服务版本前端(基于 [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue))。
* 后端采用Spring Boot、Spring Cloud & Alibaba。
* 注册中心、配置中心选型Nacos，权限认证使用Redis。
* 流量控制框架选型Sentinel，分布式事务选型Seata。
* 提供了技术栈（[Vue3](https://v3.cn.vuejs.org) [Element Plus](https://element-plus.org/zh-CN) [Vite](https://cn.vitejs.dev)）版本[RuoYi-Cloud-Vue3](https://github.com/yangzongzhuan/RuoYi-Cloud-Vue3)，保持同步更新。
* 如需不分离应用，请移步 [RuoYi](https://gitee.com/y_project/RuoYi)，如需分离应用，请移步 [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue)
* 阿里云折扣场：[点我进入](http://aly.ruoyi.vip)，腾讯云秒杀场：[点我进入](http://txy.ruoyi.vip)&nbsp;&nbsp;
* 阿里云优惠券：[点我领取](https://www.aliyun.com/minisite/goods?userCode=brki8iof&share_source=copy_link)，腾讯云优惠券：[点我领取](https://cloud.tencent.com/redirect.php?redirect=1025&cps_key=198c8df2ed259157187173bc7f4f32fd&from=console)&nbsp;&nbsp;

#### 友情链接 [若依/RuoYi-Cloud](https://gitee.com/zhangmrit/ruoyi-cloud) Ant Design版本。

## 系统模块

~~~
com.ruoyi     
├── ruoyi-ui              // 前端框架 [80]
├── ruoyi-gateway         // 网关模块 [8080]
├── ruoyi-auth            // 认证中心 [9200]
├── ruoyi-api             // 接口模块
│       └── ruoyi-api-system                          // 系统接口
├── ruoyi-common          // 通用模块
│       └── ruoyi-common-core                         // 核心模块
│       └── ruoyi-common-datascope                    // 权限范围
│       └── ruoyi-common-datasource                   // 多数据源
│       └── ruoyi-common-log                          // 日志记录
│       └── ruoyi-common-redis                        // 缓存服务
│       └── ruoyi-common-seata                        // 分布式事务
│       └── ruoyi-common-security                     // 安全模块
│       └── ruoyi-common-swagger                      // 系统接口
├── ruoyi-modules         // 业务模块
│       └── ruoyi-system                              // 系统模块 [9201]
│       └── ruoyi-gen                                 // 代码生成 [9202]
│       └── ruoyi-job                                 // 定时任务 [9203]
│       └── ruoyi-file                                // 文件服务 [9300]
├── ruoyi-visual          // 图形化管理模块
│       └── ruoyi-visual-monitor                      // 监控中心 [9100]
├──pom.xml                // 公共依赖
~~~

## 架构图

<img src="https://oscimg.oschina.net/oscnet/up-82e9722ecb846786405a904bafcf19f73f3.png"/>

## 内置功能

1.  用户管理：用户是系统操作者，该功能主要完成系统用户配置。
2.  部门管理：配置系统组织机构（公司、部门、小组），树结构展现支持数据权限。
3.  岗位管理：配置系统用户所属担任职务。
4.  菜单管理：配置系统菜单，操作权限，按钮权限标识等。
5.  角色管理：角色菜单权限分配、设置角色按机构进行数据范围权限划分。
6.  字典管理：对系统中经常使用的一些较为固定的数据进行维护。
7.  参数管理：对系统动态配置常用参数。
8.  通知公告：系统通知公告信息发布维护。
9.  操作日志：系统正常操作日志记录和查询；系统异常信息日志记录和查询。
10. 登录日志：系统登录日志记录查询包含登录异常。
11. 在线用户：当前系统中活跃用户状态监控。
12. 定时任务：在线（添加、修改、删除)任务调度包含执行结果日志。
13. 代码生成：前后端代码的生成（java、html、xml、sql）支持CRUD下载 。
14. 系统接口：根据业务代码自动生成相关的api接口文档。
15. 服务监控：监视当前系统CPU、内存、磁盘、堆栈等相关信息。
16. 在线构建器：拖动表单元素生成相应的HTML代码。
17. 连接池监视：监视当前系统数据库连接池状态，可进行分析SQL找出系统性能瓶颈。

## 在线体验

- admin/admin123  
- 陆陆续续收到一些打赏，为了更好的体验已用于演示服务器升级。谢谢各位小伙伴。

演示地址：http://ruoyi.vip  
文档地址：http://doc.ruoyi.vip

## 演示图

<table>
    <tr>
        <td><img src="https://oscimg.oschina.net/oscnet/cd1f90be5f2684f4560c9519c0f2a232ee8.jpg"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/1cbcf0e6f257c7d3a063c0e3f2ff989e4b3.jpg"/></td>
    </tr>
    <tr>
        <td><img src="https://oscimg.oschina.net/oscnet/up-8074972883b5ba0622e13246738ebba237a.png"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/up-9f88719cdfca9af2e58b352a20e23d43b12.png"/></td>
    </tr>
    <tr>
        <td><img src="https://oscimg.oschina.net/oscnet/up-39bf2584ec3a529b0d5a3b70d15c9b37646.png"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/up-4148b24f58660a9dc347761e4cf6162f28f.png"/></td>
    </tr>
	<tr>
        <td><img src="https://oscimg.oschina.net/oscnet/up-b2d62ceb95d2dd9b3fbe157bb70d26001e9.png"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/up-d67451d308b7a79ad6819723396f7c3d77a.png"/></td>
    </tr>	 
    <tr>
        <td><img src="https://oscimg.oschina.net/oscnet/5e8c387724954459291aafd5eb52b456f53.jpg"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/644e78da53c2e92a95dfda4f76e6d117c4b.jpg"/></td>
    </tr>
	<tr>
        <td><img src="https://oscimg.oschina.net/oscnet/up-8370a0d02977eebf6dbf854c8450293c937.png"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/up-49003ed83f60f633e7153609a53a2b644f7.png"/></td>
    </tr>
	<tr>
        <td><img src="https://oscimg.oschina.net/oscnet/up-d4fe726319ece268d4746602c39cffc0621.png"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/up-c195234bbcd30be6927f037a6755e6ab69c.png"/></td>
    </tr>
	<tr>
        <td><img src="https://oscimg.oschina.net/oscnet/up-ece3fd37a3d4bb75a3926e905a3c5629055.png"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/up-92ffb7f3835855cff100fa0f754a6be0d99.png"/></td>
    </tr>
    <tr>
        <td><img src="https://oscimg.oschina.net/oscnet/up-ff9e3066561574aca73005c5730c6a41f15.png"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/up-5e4daac0bb59612c5038448acbcef235e3a.png"/></td>
    </tr>
</table>


## 若依微服务交流群

QQ群： [![加入QQ群](https://img.shields.io/badge/已满-42799195-blue.svg)](https://jq.qq.com/?_wv=1027&k=yqInfq0S) [![加入QQ群](https://img.shields.io/badge/已满-170157040-blue.svg)](https://jq.qq.com/?_wv=1027&k=Oy1mb3p8) [![加入QQ群](https://img.shields.io/badge/已满-130643120-blue.svg)](https://jq.qq.com/?_wv=1027&k=rvxkJtXK) [![加入QQ群](https://img.shields.io/badge/已满-225920371-blue.svg)](https://jq.qq.com/?_wv=1027&k=0Ck3PvTe) [![加入QQ群](https://img.shields.io/badge/已满-201705537-blue.svg)](https://jq.qq.com/?_wv=1027&k=FnHHP4TT) [![加入QQ群](https://img.shields.io/badge/已满-236543183-blue.svg)](https://jq.qq.com/?_wv=1027&k=qdT1Ojpz) [![加入QQ群](https://img.shields.io/badge/已满-213618602-blue.svg)](https://jq.qq.com/?_wv=1027&k=nw3OiyXs) [![加入QQ群](https://img.shields.io/badge/已满-148794840-blue.svg)](https://jq.qq.com/?_wv=1027&k=kiU5WDls) [![加入QQ群](https://img.shields.io/badge/已满-118752664-blue.svg)](https://jq.qq.com/?_wv=1027&k=MtBy6YfT) [![加入QQ群](https://img.shields.io/badge/101038945-blue.svg)](https://jq.qq.com/?_wv=1027&k=FqImHgH2) 点击按钮入群。