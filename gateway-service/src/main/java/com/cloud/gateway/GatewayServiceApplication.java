package com.cloud.gateway;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class GatewayServiceApplication {

	/**
	 * spring cloud gateway 配置方式之一，启动主程序配置，还有一种是配置文件配置
	 * @param builder
	 * @return
	 */
//	@Bean
//	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//		return builder.routes()
//				.route(r -> r.path("/qq/**")
//							.and()
//							.uri("http://www.qq.com/"))
//				.build();
//	}

	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
		log.info("*****************");
		log.info("**** 启动成功 ****");
		log.info("*****************");
	}
}
