package com.ymt.seckill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * 功能描述：消息的消费者
 */
@Service
@Slf4j
public class MQReceiver {

    // 测试基础模式
    @RabbitListener(queues = "queue")
    public void receive(Object msg) {
        log.info("接收消息：" + msg);
    }

    // 测试fanout模式
    @RabbitListener(queues = "queue_fanout01")
    public void receive01(Object msg) {
        log.info("QUEUE01接收消息：" + msg);
    }

    @RabbitListener(queues = "queue_fanout02")
    public void receive02(Object msg) {
        log.info("QUEUE02接收消息：" + msg);
    }

    // 测试direct模式
    @RabbitListener(queues = "queue_direct01")
    public void receive03(Object msg) {
        log.info("QUEUE01接收消息：" + msg);
    }

    @RabbitListener(queues = "queue_direct02")
    public void receive04(Object msg) {
        log.info("QUEUE02接收消息：" + msg);
    }

    // 测试topic模式
    @RabbitListener(queues = "queue_topic01")
    public void receive05(Object msg) {
        log.info("QUEUE01接收消息：" + msg);
    }

    @RabbitListener(queues = "queue_topic02")
    public void receive06(Object msg) {
        log.info("QUEUE02接收消息：" + msg);
    }

    // 测试topic模式
    @RabbitListener(queues = "queue_headers01")
    public void receive07(Message msg) {
        log.info("QUEUE01接收Message对象：" + msg);
        log.info("QUEUE01接收消息：" + new String(msg.getBody()));
    }

    @RabbitListener(queues = "queue_headers02")
    public void receive08(Message msg) {
        log.info("QUEUE02接收Message对象：" + msg);
        log.info("QUEUE02接收消息：" + new String(msg.getBody()));
    }

}
