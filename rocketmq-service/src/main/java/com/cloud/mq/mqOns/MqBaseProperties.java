package com.cloud.mq.mqOns;

import lombok.Data;

/**
 * @ClassName: MqBaseProperties
 * @Description: TODO
 * @Date 2019/6/24 8:52 PM
 * @Version 1.0
 */
@Data
public class MqBaseProperties {
    /**
     * 队列主题
     */
    private String topic;

    /**
     * 队列标签
     */
    private String tag;

    private String groupId;


    /**
     * 设置代表消息的业务关键属性前缀，尽可能全局唯一
     * 以方便您在无法正常收到消息情况下，可通过MQ 控制台查询消息并补发
     */
    private String keyPrefix;
}
