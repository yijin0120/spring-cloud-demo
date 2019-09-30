package com.cloud.mq.controller;

import com.cloud.mq.service.MqSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: MessageProductController
 * @Description: 消息队列测试Controller
 * @Date 2019/6/24 11:27 PM
 * @Version 1.0
 */
@RestController
public class MessageProductController {

    private final static Logger LOGGER = LoggerFactory.getLogger(MessageProductController.class);
    @Autowired
    private MqSendService mqSendService;

    /**
     * 发送普通消息到队列
     */
    @RequestMapping("/send/normal/mess")
    public String sendNormalMess(@RequestParam("mess") String mess) {
        mqSendService.sendNormalMess(mess);
        return "success";
    }

    /**
     * 发送广播消息到队列
     */
    @RequestMapping("/send/broadcast/mess")
    public String sendBroadcastMess(@RequestParam("mess") String mess) {
        mqSendService.sendBroadcastMess(mess);
        return "success";
    }

    /**
     * 批量发送普通消息到队列
     */
    @RequestMapping("/send/many-normal/mess")
    public String sendManyNormalMess(@RequestParam("mess") String mess) {
        LOGGER.info("批量发送消息测试开始了 ");
        for (int i = 0; i < 10; i++) {
            mqSendService.sendNormalMess(mess.concat(String.valueOf(i)));
        }
        LOGGER.info("批量发送消息测试完毕了 ");
        return "success";
    }

    /**
     * 发送延时消息到队列
     */
    @RequestMapping("/send/delay/mess")
    public String sendDelayMess(@RequestParam("mess") String mess) {
        mqSendService.sendDelayMess(mess);
        return "success";
    }
}
