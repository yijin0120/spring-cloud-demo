package com.cloud.quartz.remote;

import com.cloud.common.enums.ApiMsgEnum;
import com.cloud.common.vo.BaseReturnVO;
import org.springframework.stereotype.Component;

/**
 * 类或方法的功能描述 : 用户服务调用熔断
 *
 */
@Component
public class UserClientHystrix implements UserClient {
	@Override
	public String getPort() {
		return "order service 调用失败！";
	}
	@Override
	public BaseReturnVO getUserById(Integer id) {
		BaseReturnVO baseReturnVO = new BaseReturnVO();
		baseReturnVO.setResCode(ApiMsgEnum.INTERNAL_SERVER_ERROR.getResCode());
		baseReturnVO.setResDes("user service getUserById 调用失败！");
		return baseReturnVO;
	}
}

