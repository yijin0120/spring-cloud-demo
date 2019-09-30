package com.cloud.mq.service;


import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName: MessageHandlerService
 * @Description: 消息处理
 * @Date 2019/7/1 12:03 AM
 * @Version 1.0
 */
public interface MessageHandlerService {

    void handler(JSONObject message);
}
