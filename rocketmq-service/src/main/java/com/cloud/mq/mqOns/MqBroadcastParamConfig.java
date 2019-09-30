package com.cloud.mq.mqOns;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName: MqBroadcastParamConfig
 * @Description: 广播消息
 * @Date 2019/6/24 8:55 PM
 * @Version 1.0
 */
@Component
@ConfigurationProperties(prefix = "aliyun.ons.broadcast")
public class MqBroadcastParamConfig extends MqBaseProperties {
}
