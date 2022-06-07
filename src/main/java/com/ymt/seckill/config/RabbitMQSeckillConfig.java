package com.ymt.seckill.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类 - Topic模式
 */
@Configuration
public class RabbitMQSeckillConfig {
    // Topic模式对应的队列和交换机、路由件
    private static final String QUEUE = "seckillQueue";
    private static final String EXCHANGE = "seckillExchange";

    @Bean
    public Queue seckillQueue() {
        return new Queue(QUEUE);  // 没有像基础中的queue一样开启持久化
    }


    @Bean
    public TopicExchange seckillExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding seckillBinding() {
        return BindingBuilder.bind(seckillQueue()).to(seckillExchange()).with("seckill.#");
    }


}
