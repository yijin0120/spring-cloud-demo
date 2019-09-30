package com.cloud.mq.listener;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.shade.org.apache.commons.lang3.exception.ExceptionUtils;
import com.cloud.mq.service.MessageHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName: NormalMessageListener
 * @Description: 普通消息监听
 * @Date 2019/6/24 9:02 PM
 * @Version 1.0
 */
@Component
public class NormalMessageListener extends MqBaseListener implements MessageListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(NormalMessageListener.class);
    @Autowired
    private MessageHandlerService mqTestService;

    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        LOGGER.info("进入普通消息队列监听 message={}", new String(message.getBody()));
        LOGGER.info("消息 id={},执行Host={}", message.getMsgID(), message.getBornHost());
        LOGGER.info("消息 Topic={},Tag={}", message.getTopic(), message.getTag());
        LOGGER.info("消息生成时间={}", message.getBornTimestamp());
        LOGGER.info("消息执行次数={}", message.getReconsumeTimes());

        //获取消息转成字符串
        String srtMsg = this.getStringMess(message);
        if (null == srtMsg) {
            //消息体获取失败-> 进行重试
            return Action.ReconsumeLater;
        }
        //是否执行成功
        boolean successFlg = true;
        try {
            //反序列化为对象

            JSONObject jsonObject = JSONObject.parseObject(srtMsg);
            mqTestService.handler(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            successFlg = false;
            String stack = ExceptionUtils.getMessage(e);
            LOGGER.info("[消费处理异常] {}", stack);

        }
        //判断当前执行次数是否达到上限
        boolean canRetry = this.canRetryFiveTimes(message.getReconsumeTimes());
        //执行失败并且重试次数小于5 次 -> 进行消息重试
        if (!successFlg && canRetry) {
            return Action.ReconsumeLater;
        }
        LOGGER.debug("消息处理成功");
        return Action.CommitMessage;
    }
}
