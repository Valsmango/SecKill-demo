package com.ymt.seckill.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类 - Fanout模式
 */
@Configuration
public class RabbitMQFanoutConfig {

    // Fanout模式对应的队列和交换机
    private static final String QUEUE01 = "queue_fanout01";
    private static final String QUEUE02 = "queue_fanout02";
    private static final String EXCHANGE = "fanoutExchange";

    @Bean
    public Queue queue01() {
        return new Queue(QUEUE01);  // 没有像基础中的queue一样开启持久化
    }

    @Bean
    public Queue queue02() {
        return new Queue(QUEUE02);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(EXCHANGE);
    }

    /**
     * 将队列绑定到交换机上
     */
    @Bean
    public Binding binding01() {
        return BindingBuilder.bind(queue01()).to(fanoutExchange());
    }

    @Bean
    public Binding binding02() {
        return BindingBuilder.bind(queue02()).to(fanoutExchange());
    }
}
