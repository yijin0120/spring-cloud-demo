spring cloud demo 项目
-
#### 项目使用到的技术
1.Spring cloud  
* eureka注册中心
* gateway网关
* feign声明式调用
* ribbon负载均衡
* hystrix熔断机制  
* zipkin服务追踪治理
    
2.Spring boot  
3.Redis  

#### 项目框架版本
- spring cloud Finchley.RELEASE
- spring boot 2.x
- maven 3.3.9
- jdk 1.8
- mysql 5.9
- redis 2.9

#### 服务说明
- eureka-service 注册中心，端口号 5000
- gateway-service 网关服务，端口号 5001
- common-service 通用服务，端口号 5005
- user-service 用户服务，端口号 5200



##### 简要说明
由于使用了网关所以所有的服务都统一个地址访问 http://localhost:5001/*， 为了方便测试服务是否正常
在每个服务中都有一个 getPort() 获取端口号的接口，比如：</br>
http://localhost:5001/ADDRESS-SERVICE/address/getPort </br>
http://localhost:5001/ORDER-SERVICE/order/getPort </br>
由于使用了spring cloud RC版本的网关gateWay，所以在访问的时候采取的是默认的服务名大写的方式访问。

address-service 使用的技术框架比较全，包含redis,mongodb,rabbitmq

address-service使用了ribbon负载均衡，在跨服务调用order-service的时候有两种方式调用<br/>
1.使用feign调用，启动服务后浏览器访问 http://localhost:5001/ADDRESS-SERVICE/address/feign/getFeignOrderById/2 <br/>
2.使用ribbon调用，首选需要使用idea启动2个服务，先启动一个order-service，然后修改端口号为4100后再启动一个此时在
eureka注册中心可以看到两个order-service（idea怎么启动两个服务参照：https://blog.csdn.net/zxl646801924/article/details/81207089）<br/>
启动两个order-service后再启动address-service服务，然后浏览器多次访问：<br/>
http://localhost:5001/ADDRESS-SERVICE/address/ribbon/getRibbonOrderPort <br/>
就可以得到如下结果： <br/>
order-service port：5100 <br/>
order-service port：4100 <br/>  

user-service 增加了AOP切面，通过调用 http://localhost:5001/USER-SERVICE/user/getUserById/1 就可以看到控制台上输出的日志，AOP切面做了简单的日志输入。  

**redis:**
redis 可视化工具使用 redis desktop manager 下载安装连接到redis即可使用

**zipkin**
Spring Boot 2.0 版本之后，zipkin官方已不推荐自己搭建定制了，而是直接提供了编译好的 jar 包。
详情可以查看官网：https://zipkin.io/pages/quickstart.html
<br/>
可以在终端使用以下命令：
<br/>
curl -sSL https://zipkin.io/quickstart.sh | bash -s     <br/>
java -jar zipkin.jar
<br/>
使用 docker 的方式
<br/>
docker run -d -p 9411:9411 openzipkin/zipkin
<br/><br/>
Windows可以下载zipkin的jar包，地址：https://search.maven.org/remote_content?g=io.zipkin.java&a=zipkin-server&v=LATEST&c=exec
<br/>
下载后可以使用 java -jar zipkin.jar命令即可本地启动
<br/>
任一方式启动后，访问 http://localhost:9411，可以看到服务端已经搭建成功


