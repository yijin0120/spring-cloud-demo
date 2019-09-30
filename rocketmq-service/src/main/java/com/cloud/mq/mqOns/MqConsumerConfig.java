package com.cloud.mq.mqOns;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import com.cloud.mq.listener.BroadcastMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @ClassName: MqConsumerConfig
 * @Description: 消费者配置
 * @Date 2019/6/24 8:58 PM
 * @Version 1.0
 */
@Configuration
public class MqConsumerConfig extends MqBaseConfig {
    private final static Logger LOGGER = LoggerFactory.getLogger(MqConsumerConfig.class);
    @Autowired
    private MqNormalParamConfig normalParamConfig;
    @Autowired
    private MqBroadcastParamConfig broadcastParamConfig;
    @Autowired
    private MessageListener normalMessageListener;
    /**
     * 创建 普通消费者
     * 默认： 集群订阅(多实例可防重)
     */
    @Bean(name = "normalConsumer01", initMethod = "start", destroyMethod = "shutdown")
    public ConsumerBean normalConsumerBer() {
        LOGGER.info("{} 消费者创建开始", "normalConsumer");
        //消费固定线程数
        String consumeThreadNum = "3";
        Map<Subscription, MessageListener> subscriptionTable =
                this.createSubscriptionTable(normalParamConfig.getTopic(), normalMessageListener);
        //集群订阅消费者
        ConsumerBean consumerBean =
                this.createConsumer(consumeThreadNum, subscriptionTable);
        consumerBean.setSubscriptionTable(subscriptionTable);
        LOGGER.info("{} 消费者创建完毕", "normalConsumer");
        return consumerBean;
    }

    /**
     * 创建 广播消费者
     * 默认： 广播订阅
     */
   @Bean(name = "broadcastConsumer", initMethod = "start", destroyMethod = "shutdown")
    public ConsumerBean broadcastConsumer() {
        LOGGER.info("{} 广播订阅消费者创建开始", "broadcastConsumer");
        //消费固定线程数
        String consumeThreadNum = "3";
        Map<Subscription, MessageListener> subscriptionTable =
                this.createSubscriptionTable(broadcastParamConfig.getTopic(),
                        new BroadcastMessageListener());
        LOGGER.info("广播订阅消费者 size={}", subscriptionTable.size());
        //广播订阅消费者
        ConsumerBean consumerBean =
                this.createBbRoadCastConsumer(consumeThreadNum, subscriptionTable);
        consumerBean.setSubscriptionTable(subscriptionTable);
        LOGGER.info("{} 广播订阅消费者创建完毕", "broadcastConsumer");
        return consumerBean;
    }
}
