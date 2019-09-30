package com.cloud.mq.mqOns;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName: MqNormalParamConfig
 * @Description: 普通消息
 * @Date 2019/6/24 8:54 PM
 * @Version 1.0
 */
@Component
@ConfigurationProperties(prefix = "aliyun.ons.normal")
public class MqNormalParamConfig extends MqBaseProperties {

}
