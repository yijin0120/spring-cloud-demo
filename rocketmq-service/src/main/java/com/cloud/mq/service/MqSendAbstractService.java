package com.cloud.mq.service;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: MqSendAbstractService
 * @Description: 发送消息到消息队列 抽象公共方法
 * @Date 2019/6/24 9:14 PM
 * @Version 1.0
 */

public class MqSendAbstractService {
    private final static Logger LOGGER = LoggerFactory.getLogger(MqSendAbstractService.class);

    /**
     * 同步发送消息
     */
    public boolean send(Message msg, Producer currentProducer) {
        try {
            //执行发送
            SendResult sendResult = currentProducer.send(msg);
            assert sendResult != null;
            LOGGER.info("列发送消息成功 sendResult={}", sendResult);
            return true;
        } catch (ONSClientException e) {
            System.out.println("发送失败");
            LOGGER.info("消息发送失败 ", e);
            //出现异常意味着发送失败，为了避免消息丢失，建议缓存该消息然后进行重试。
            return false;
        }
    }
}
