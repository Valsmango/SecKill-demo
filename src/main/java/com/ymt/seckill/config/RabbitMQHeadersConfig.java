package com.ymt.seckill.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ配置类 - Headers模式
 */
@Configuration
public class RabbitMQHeadersConfig {

    private static final String QUEUE01 = "queue_headers01";
    private static final String QUEUE02 = "queue_headers02";
    private static final String EXCHANEG = "headersExchange";

    @Bean
    public Queue queue07() {
        return new Queue(QUEUE01);
    }

    @Bean
    public Queue queue08() {
        return new Queue(QUEUE02);
    }

    @Bean
    public HeadersExchange headersExchange() {
        return new HeadersExchange(EXCHANEG);
    }

    @Bean
    public Binding binding07() {
        Map<String, Object> map = new HashMap<>();
        map.put("color", "red");
        map.put("speed", "slow");
        return BindingBuilder.bind(queue07()).to(headersExchange()).whereAny(map).match();
    }

    @Bean
    public Binding binding08() {
        Map<String, Object> map = new HashMap<>();
        map.put("color", "red");
        map.put("speed", "fast");
        return BindingBuilder.bind(queue08()).to(headersExchange()).whereAll(map).match();
    }

}
