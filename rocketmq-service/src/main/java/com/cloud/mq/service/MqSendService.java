package com.cloud.mq.service;

/**
 * @ClassName: MqSendService
 * @Description: TODO
 * @Date 2019/6/24 10:57 PM
 * @Version 1.0
 */
public interface MqSendService {

    public void sendNormalMess(String mess);

    public void sendBroadcastMess(String mess);

    public void sendDelayMess(String mess);
}
