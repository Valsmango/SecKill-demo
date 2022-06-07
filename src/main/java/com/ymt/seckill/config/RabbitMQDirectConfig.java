//package com.ymt.seckill.config;
//
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.DirectExchange;
//import org.springframework.amqp.core.Queue;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * RabbitMQ配置类 - Direct模式
// */
//@Configuration
//public class RabbitMQDirectConfig {
//
//    // Direct模式对应的队列和交换机、路由件
//    private static final String QUEUE01 = "queue_direct01";
//    private static final String QUEUE02 = "queue_direct02";
//    private static final String EXCHANGE = "directExchange";
//    private static final String ROUTINGKEY01 = "queue.red";
//    private static final String ROUTINGKEY02 = "queue.green";
//
//    @Bean
//    public Queue queue03() {
//        return new Queue(QUEUE01);  // 没有像基础中的queue一样开启持久化
//    }
//
//    @Bean
//    public Queue queue04() {
//        return new Queue(QUEUE02);
//    }
//
//    @Bean
//    public DirectExchange directExchange() {
//        return new DirectExchange(EXCHANGE);
//    }
//
//    @Bean
//    public Binding binding03() {
//        return BindingBuilder.bind(queue03()).to(directExchange()).with(ROUTINGKEY01);
//    }
//
//    @Bean
//    public Binding binding04() {
//        return BindingBuilder.bind(queue04()).to(directExchange()).with(ROUTINGKEY02);
//    }
//}
