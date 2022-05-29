# spring-cloud-study-notes

SpringCloud 学习笔记


## 计划

- 集成MBG插件
- 集成swagger
- 集成nacos
- 把swagger的公共属性放到nacos中，把配置中心功能用起来
- 

## 模块讲解

// todo


## 项目更新日志

- 集成 MBG 插件，自动生成代码
- 编写测试controller，测试项目运行情况
- 集成nacos注册中心
- 集成swagger
- 集成nacos配置中心
  - 使用application配置文件直接启动
  - 使用application多环境启动
  - 使用bootstrap+nacos启动
  - 使用bootstrap多环境+nacos启动
  - 实际开发过程中的使用模式
    - 项目中只有bootstrap和bootstrap多环境文件
    - 不同环境上的需要配置到不同的命名空间

## 分布式事务

## 分布式锁



## 电商下单

- 商品信息
  - id、name、num、price、
- 订单
  - id、goodsId、userId
  - 
- 库存
- 金额


用户故事：用户在电商平台上进行下单。这个过程需要生成一个订单，然后扣减库存，再扣减金额；




