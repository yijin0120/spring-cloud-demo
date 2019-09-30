package com.cloud.mq.listener;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.shade.org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * @ClassName: MqBaseListener
 * @Description: 父类消息监听
 * @Date 2019/6/24 9:02 PM
 * @Version 1.0
 */
public class MqBaseListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(MqBaseListener.class);

    private Integer FIVE_TIMES = 5;


    /**
     * 获取字符串类型消息体
     *
     * @param message 消息信息
     */
    public String getStringMess(Message message) {
        //获取消息转成字符串
        String msg = null;
        try {
            msg = new String(message.getBody(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            String stack = ExceptionUtils.getMessage(e);
            LOGGER.info("消息监听->[获取消息体异常] stack={}", stack);
        }
        return msg;
    }


    /**
     * 是否可以重试 5 次
     *
     * @param runTime 当前执行次数
     * @return
     */
    public Boolean canRetryFiveTimes(int runTime) {
        return this.canRetryTimes(runTime, FIVE_TIMES);
    }

    /**
     * 是否可以重试;
     *
     * @param runTime    当前执行次数
     * @param retryTimes 重试次数
     */
    public Boolean canRetryTimes(int runTime, int retryTimes) {
        if (runTime < retryTimes) {
            return true;
        }
        return false;
    }
}
