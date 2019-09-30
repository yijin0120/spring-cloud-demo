package com.cloud.mq.listener;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;

/**
 * @ClassName: BroadcastMessageListener
 * @Description: TODO
 * @Date 2019/6/24 9:11 PM
 * @Version 1.0
 */
public class BroadcastMessageListener extends MqBaseListener implements MessageListener {
    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        return null;
    }
}
