package com.cloud.mq.mqOns;

import com.aliyun.openservices.ons.api.bean.ProducerBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: MqProducerConfig
 * @Description: 生产者配置
 * @Date 2019/6/24 8:50 PM
 * @Version 1.0
 */
@Configuration
public class MqProducerConfig extends MqBaseConfig {
    private final static Logger LOGGER = LoggerFactory.getLogger(MqProducerConfig.class);

    /**
     * 创建 普通生产者
     */
    @Bean(name = "normalProducer", initMethod = "start", destroyMethod = "shutdown")
    public ProducerBean normalProducer() {
        ProducerBean producerBean = this.createProducer();
        LOGGER.info("{} 生产者创建完毕", "normalProducer");
        return producerBean;
    }

    /**
     * 创建 广播订阅消息 生产者
     */
    @Bean(name = "broadcastProducer", initMethod = "start", destroyMethod = "shutdown")
    public ProducerBean broadcastProducer() {
        ProducerBean producerBean = this.createProducer();
        LOGGER.info("{} 生产者创建完毕", "broadcastProducer");
        return producerBean;
    }
}
