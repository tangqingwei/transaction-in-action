## 说明
### 第一步：pom文件加入springcloud
        <!--注册中心-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <!--spring boot-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
### 第二步：创建OrderApplication启动文件
### 第三步：创建application.properties配置文件

### 第四步：引入mybatis自动生成类
1.采用mybatis-generator自动生成文件
2.pom文件加入mybatis的依赖包
3.配置文件，增加数据库配置信息
4.启动类
    @MapperScan("com.qige.transaction.order.mapper")
### 第五步：实现服务之间的feign通信
1.在pom文件中加入依赖的feign jar包
2.在启动类加入feign的注解
    @EnableFeignClients(basePackages = {"com.qige.transaction.inventory.api"})
### 第六步：引入hmily的分布式事务框架
1.pom加入分布式事务框架的依赖包
2.配置文件改动
    加入hmily的配置文件applicationContext.xml
3.启动类改动
    @ImportResource({"classpath:applicationContext.xml"})
    @SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
