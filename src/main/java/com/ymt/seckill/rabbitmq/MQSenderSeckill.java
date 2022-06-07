package com.ymt.seckill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 功能描述：消息发送者
 * 调用config中RabbitMQConfig中的方法
 */
@Service
@Slf4j
public class MQSenderSeckill {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 基于Topic模式
    public void sendSeckillMessage(String message) {
        log.info("发送消息：" + message);
        rabbitTemplate.convertAndSend("seckillExchange", "seckill.message", message);   // 只能被QUEUE01接收"#.queue.#"
    }
}
