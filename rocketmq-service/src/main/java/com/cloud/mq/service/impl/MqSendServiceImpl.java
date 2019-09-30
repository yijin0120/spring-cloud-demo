package com.cloud.mq.service.impl;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.cloud.mq.mqOns.MqBroadcastParamConfig;
import com.cloud.mq.mqOns.MqNormalParamConfig;
import com.cloud.mq.service.MqSendAbstractService;
import com.cloud.mq.service.MqSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


/**
 * @ClassName: MqSendServiceImpl
 * @Description: TODO
 * @Date 2019/6/24 10:54 PM
 * @Version 1.0
 */
@Service
public class MqSendServiceImpl extends MqSendAbstractService implements MqSendService {
    private final static Logger LOGGER = LoggerFactory.getLogger(MqSendServiceImpl.class);
    @Autowired
    @Qualifier("normalProducer")
    private Producer normalProducer;
    @Autowired
    @Qualifier("broadcastProducer")
    private Producer broadcastProducer;

    @Autowired
    private MqNormalParamConfig normalParamConfig;

    @Autowired
    private MqBroadcastParamConfig broadcastParamConfig;
    /**
     * 普通消息
     *
     * @param mess 消息内容
     *             Message(String topic, String tags, byte[] body)  参数介绍
     *             topic :消息所属Topic (主题)
     *             tags :消息标签，二级消息类型，用来进一步区分某个 Topic 下的消息分类。
     *             对消息进行再归类，方便Consumer指定过滤条件在MQ服务器过滤
     *             body：消息体 可以是任何二进制形式的数据， MQ不做任何干预
     *             注意需要Producer与Consumer协商好一致的序列化和反序列化方式
     *             key:设置代表消息的业务关键属性，请尽可能全局唯一
     *             方便在无法正常收到消息情况下，可通过MQ 控制台查询消息并补发
     *             注意：不设置也不会影响消息正常收发
     */
    @Override
    public void sendNormalMess(String mess) {
        LOGGER.info("发送普通消息开始");
        Message msg = new Message(normalParamConfig.getTopic(),
                normalParamConfig.getTag(),
                mess.getBytes());
        LOGGER.info("普通消息 msg={}", msg);
        //消息表示前缀
        msg.setKey(normalParamConfig.getKeyPrefix().concat(mess));
        // 发送消息，只要不抛异常就是成功
        this.send(msg, normalProducer);
    }


    /**
     * 广播消息
     *
     * @param mess 消息内容
     */
    @Override
    public void sendBroadcastMess(String mess) {
        LOGGER.info("发送广播消息开始");
        Message msg = new Message(broadcastParamConfig.getTopic(),
                broadcastParamConfig.getTag(),
                mess.getBytes());
        LOGGER.info("广播消息 msg={}", msg);
        //消息表示前缀
        msg.setKey(broadcastParamConfig.getKeyPrefix().concat(mess));
        // 发送消息，只要不抛异常就是成功
        this.send(msg, broadcastProducer);//不采用广播，先注销掉
    }

    /**
     * 延时消息
     *
     * @param mess 消息内容
     */
    @Override
    public void sendDelayMess(String mess) {
        Message msg = new Message(normalParamConfig.getTopic(),
                normalParamConfig.getTag(),
                mess.getBytes());
        LOGGER.info("发送延时消息开始");
        //消息表示前缀
        msg.setKey(normalParamConfig.getKeyPrefix().concat(mess));
        //时间偏移1 分钟
       // long time = DateUtil.offsetMinute(new Date(), 1).getTime();
        long time = System.currentTimeMillis();
        msg.setStartDeliverTime(time);

        LOGGER.info("延时消息 msg={}", msg);
        // 发送消息，只要不抛异常就是成功
        this.send(msg, normalProducer);
    }

}
