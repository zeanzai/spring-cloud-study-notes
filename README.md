## 教程地址

文档地址： 语雀-Spring-SpringCloudAlibaba实战

下面的废弃

---


[你真的了解技术选型吗？.md](docs%2F%C4%E3%D5%E6%B5%C4%C1%CB%BD%E2%BC%BC%CA%F5%D1%A1%D0%CD%C2%F0%A3%BF.md)
[分布式服务治理之Nacos.md](docs%2F%B7%D6%B2%BC%CA%BD%B7%FE%CE%F1%D6%CE%C0%ED%D6%AENacos.md)

[分布式服务网关之Gateway.md](docs%2F%B7%D6%B2%BC%CA%BD%B7%FE%CE%F1%CD%F8%B9%D8%D6%AEGateway.md)

[分布式服务调用之OpenFeign.md](docs%2F%B7%D6%B2%BC%CA%BD%B7%FE%CE%F1%B5%F7%D3%C3%D6%AEOpenFeign.md)
[分布式服务配置之nacos.md](docs%2F%B7%D6%B2%BC%CA%BD%B7%FE%CE%F1%C5%E4%D6%C3%D6%AEnacos.md)
[基于Centos7安装Docker.md](docs%2F%BB%F9%D3%DACentos7%B0%B2%D7%B0Docker.md)

[如何在SpringCloud项目中统一管理依赖的版本？.md](docs%2F%C8%E7%BA%CE%D4%DASpringCloud%CF%EE%C4%BF%D6%D0%CD%B3%D2%BB%B9%DC%C0%ED%D2%C0%C0%B5%B5%C4%B0%E6%B1%BE%A3%BF.md)

[基于SpringCloud-Alibaba的微服务实战演练](https://zeanzai.me/springcloud-alibaba/#%E8%AF%B4%E6%98%8E)

- 前言
  - [你真的了解技术选型吗？](https://zeanzai.me/springcloud-alibaba/before/chapterA.html)
  - [如何在SpringCloud项目中统一管理依赖的版本？](https://zeanzai.me/springcloud-alibaba/before/chapterB.html)
  - [使用idea插件生成代码](https://zeanzai.me/springcloud-alibaba/before/easycode.html)
- [分布式服务治理之Nacos](https://zeanzai.me/springcloud-alibaba/chapter01.html)
- [分布式服务配置之nacos](https://zeanzai.me/springcloud-alibaba/chapter02.html)
- [分布式服务调用之OpenFeign](https://zeanzai.me/springcloud-alibaba/chapter03.html)
- [分布式服务网关之Gateway](https://zeanzai.me/springcloud-alibaba/chapter04.html)
- 分布式事务管理之Seata
  - [分布式事务概论](https://zeanzai.me/springcloud-alibaba/seata/distribute-transaction.html)
  - [搭建Seata服务器端](https://zeanzai.me/springcloud-alibaba/seata/install-seata.html)
  - [Seata分布式事务之AT模式](https://zeanzai.me/springcloud-alibaba/seata/seata-at.html)
  - [Seata分布式事务之TCC模式](https://zeanzai.me/springcloud-alibaba/seata/seata-tcc.html)

TODO

- [全局异常实现国际化+可配置化]() ： 统一响应结果、全局异常实现国际化+可配置化
  - us语言的配置测试；
  - 再次添加其他配置项不能成功使用
- [接口幂等性实现]()： 
- [基于Redis实现分布式锁]()：
- [基于Redis实现接口限流]()：
- []()：



## 功能描述

- nacos-as-register 使用nacos作为注册中心
  - nacos-provider: 10000
  - nacos-consumer: 10100
- nacos-as-configer: 10200 ，使用nacos作为配置中心
- nacos-global-exception: 10300 ，全局异常+国际化+可配置化
- gateway-demo: 10400 ， SpringGateway实战
- swagger ， 使用SpringGateway集成Swagger
  - gateway-swagger: 10500
  - swagger-user: 10600
  - swagger-order: 10700
- seata-tcc ， Seata的TCC模式的实现
  - seata-tcc-account: 10801
  - seata-tcc-storage: 10802
  - seata-tcc-order: 10803
- seata-at ， Seata的AT模式的实现
  - seata-at-account: 10901
  - seata-at-storage: 10902
  - seata-at-order: 10903
- sharding-notes ， 分库分表， 具体见模块内README.md
- safe-api
- jvm-test
- practice
  - springboot-rabbitmq








    

    


