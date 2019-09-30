package com.cloud.mq.mqOns;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.PropertyValueConst;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @ClassName: MqBaseConfig
 * @Description: TODO
 * @Date 2019/6/24 8:43 PM
 * @Version 1.0
 */
public class MqBaseConfig {
    private final static Logger LOGGER = LoggerFactory.getLogger(MqBaseConfig.class);

    private final String STAR_FLOWER = "*";

    @Value("${aliyun.accessKey}")
    private String accessKey;
    @Value("${aliyun.secretKey}")
    private String secretKey;
    @Value("${aliyun.ons.normal.groupId}")
    private String normalGroupId;
    @Value("${aliyun.ons.addr}")
    private String onsAddr;
    /**
     * 创建生产者
     *
     * @return 生产者bean
     */
    protected ProducerBean createProducer() {
        ProducerBean producerBean = new ProducerBean();
        Properties properties = this.createProducerProperties();
        producerBean.setProperties(properties);
        LOGGER.info("创建生产者参数 producerBean={}}", producerBean);
        return producerBean;
    }

    /**
     * 创建集群订阅消费者
     *
     * @return 消费者bean
     */
    protected ConsumerBean createConsumer(String consumeThreadNum,
                                          Map<Subscription, MessageListener> subscriptionTable) {
        ConsumerBean consumerBean = new ConsumerBean();
        Properties properties = this.createConsumerProperties(consumeThreadNum);
        consumerBean.setProperties(properties);
        consumerBean.setSubscriptionTable(subscriptionTable);
        LOGGER.info("创建消费者参数 groupId={}", normalGroupId);
        return consumerBean;
    }

    /**
     * 创建生产者属性参数
     *
     * @return 生产者属性参数
     */
    private Properties createProducerProperties() {
        Properties properties = this.buildBaseProperties();
        //生产者id
        properties.setProperty(PropertyKeyConst.GROUP_ID, normalGroupId);
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR,onsAddr);
        return properties;
    }

    /**
     * 创建消费者监听Map
     * key:订阅相关类 (Subscription )
     * value: 消费者监听(MessageListener)
     *
     * @param topic     消息主题
     * @param listeners 监听队列，可设置多个
     * @return 消费者监听Map
     */
    protected Map<Subscription, MessageListener> createSubscriptionTable(String topic, MessageListener listeners) {
        //expression即Tag，可以设置成具体的Tag，如 taga||tagb||tagc，也可设置成*。
        // *仅代表订阅所有Tag，不支持通配
        return this.createSubscriptionTable(topic, STAR_FLOWER, listeners);
    }


    /**
     * 创建消费者监听Map
     * key:订阅相关类 (Subscription )
     * value: 消费者监听(MessageListener)
     *
     * @param topic     消息主题
     * @param tag       消息标签
     * @param listeners 监听队列，可设置多个
     * @return 消费者监听Map
     */
    protected Map<Subscription, MessageListener> createSubscriptionTable(String topic,
                                                                         String tag, MessageListener listeners) {
        //创建监听
        Subscription subscription = new Subscription();
        //主题
        subscription.setTopic(topic);
        //标签
        subscription.setExpression(tag);
        LOGGER.info("消费者创建 参数 subscription={}", subscription);
        Map<Subscription, MessageListener> subscriptionTable = new HashMap<>();
        if (!ObjectUtils.isEmpty(listeners)) {
            subscriptionTable.put(subscription, listeners);
        }
        LOGGER.info("消费者创建 参数 subscriptionTableSize={}", subscriptionTable.size());
        return subscriptionTable;
    }



    /**
     * 创建消费者属性参数
     * 默认 集群订阅
     *
     * @param consumeThreadNum 最大线程数
     * @return 消费者属性参数
     */
    private Properties createConsumerProperties(String consumeThreadNum) {
        return this.createConsumerProperties(consumeThreadNum, false);
    }
    /**
     * 创建消费者属性参数
     *
     * @param consumeThreadNum 最大线程数
     * @param isBbRoadCast     是否是广播订阅队列
     * @return 消费者属性参数
     * 集群订阅：默认模式 ，相同消费者id所有消费者平均分摊消费消息。
     * 例如某个 Topic 有 9 条消息，一个 Consumer ID 有 3 个 Consumer 实例，
     * 那么在集群消费模式下每个实例平均分摊，只消费其中的 3 条消息。
     * <p>
     * 广播订阅：相同消费者id 所标识的所有消费者都会各自消费某条消息一次。
     * 例如某个 Topic 有 9 条消息，一个 Consumer ID 有 3 个 Consumer 实例，
     * 那么在广播消费模式下每个实例都会各自消费 9 条消息。
     */
    private Properties createConsumerProperties(String consumeThreadNum, boolean isBbRoadCast) {
        Properties properties = this.buildBaseProperties();
        //消费者 id
        properties.setProperty(PropertyKeyConst.GROUP_ID,normalGroupId);
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR,onsAddr);
        //固定消费者线程数为xx个
        properties.setProperty(PropertyKeyConst.ConsumeThreadNums, consumeThreadNum);
        if (isBbRoadCast) {
            LOGGER.info("广播模式消费者 groupId={}", normalGroupId);
            properties.put(PropertyKeyConst.MessageModel, PropertyValueConst.BROADCASTING);
        }
        return properties;
    }
    /**
     * 创建广播模式消费者
     *
     * @param consumeThreadNum  固定消费者线程数为xx个
     * @param subscriptionTable 消费监听Map
     * @return 消费者bean
     */
    protected ConsumerBean createBbRoadCastConsumer (String consumeThreadNum,
                                                    Map<Subscription, MessageListener> subscriptionTable) {
        ConsumerBean consumerBean = new ConsumerBean();
        Properties properties = this.createConsumerProperties(consumeThreadNum, true);
        consumerBean.setProperties(properties);
        consumerBean.setSubscriptionTable(subscriptionTable);
        LOGGER.info("创建消费者参数 consumerBean={}", consumerBean);
        return consumerBean;
    }

    /**
     * 公共属性参数
     */
    private Properties buildBaseProperties() {
        Properties properties = new Properties();
        //阿里云 Access Key
        properties.setProperty(PropertyKeyConst.AccessKey, accessKey);
        //阿里云 Access secret key
        properties.setProperty(PropertyKeyConst.SecretKey, secretKey);
        return properties;
    }

}
