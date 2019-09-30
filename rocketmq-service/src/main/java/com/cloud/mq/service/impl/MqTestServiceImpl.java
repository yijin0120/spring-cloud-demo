package com.cloud.mq.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cloud.mq.service.MessageHandlerService;
import org.springframework.stereotype.Service;

/**
 * @ClassName: MqTestServiceImpl
 * @Description: TODO
 * @Date 2019/8/11 4:01 PM
 * @Version 1.0
 */

@Service("mqTestService")
public class MqTestServiceImpl implements MessageHandlerService {

    @Override
    public void handler(JSONObject message) {
        System.out.println("----------------------消息消费成功--------------------");
        System.out.println(JSONObject.toJSONString(message));
        System.out.println("----------------------消息消费成功--------------------");
    }

}
