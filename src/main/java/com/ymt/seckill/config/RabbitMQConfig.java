package com.ymt.seckill.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类 - 基础案例
 */
@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue queue() {
//        return new Queue("queue", true);
        return new Queue("queue");
    }
}
