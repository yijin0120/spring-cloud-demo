package com.cloud.quartz.remote;

import com.cloud.common.vo.BaseReturnVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 类或方法的功能描述 :调用用户服务
 *
 */
@FeignClient(value = "user-service", fallback = UserClientHystrix.class)
public interface UserClient {
	@GetMapping("/user/getPort")
	String getPort();

	@GetMapping("/user/getUserById/{id}")
	BaseReturnVO getUserById(@PathVariable("id") Integer id);
}

