//package com.ymt.seckill.rabbitmq;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.core.MessageProperties;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
///**
// * 功能描述：消息发送者
// * 调用config中RabbitMQConfig中的方法
// */
//@Service
//@Slf4j
//public class MQSender {
//
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//    // 基础案例
//    public void send(Object msg) {
//        log.info("发送消息：" + msg);
//        rabbitTemplate.convertAndSend("queue",msg);
//    }
//
//    // 测试Fanout模式
//    public void send01(Object msg) {
//        log.info("发送消息：" + msg);
//        rabbitTemplate.convertAndSend("fanoutExchange","", msg);
//    }
//
//    // 测试Dircet模式
//    public void send02(Object msg) {
//        log.info("发送red消息：" + msg);
//        rabbitTemplate.convertAndSend("directExchange","queue.red", msg);
//    }
//    public void send03(Object msg) {
//        log.info("发送green消息：" + msg);
//        rabbitTemplate.convertAndSend("directExchange","queue.green", msg);
//    }
//
//    // 测试Topic模式
//    public void send04(Object msg) {
//        log.info("发送消息（QUEUE01接收）：" + msg);
//        rabbitTemplate.convertAndSend("topicExchange", "queue.red.message", msg);   // 只能被QUEUE01接收"#.queue.#"
//    }
//    public void send05(Object msg) {
//        log.info("发送消息（QUEUE01和QUEUE02都接收）：" + msg);
//        rabbitTemplate.convertAndSend("topicExchange", "both.queue.red.message", msg);   // 被01接收"#.queue.#"， 被02接收"*.queue.#"
//    }
//
//    // 测试Headers模式
//    public void send06(String msg) {
//        log.info("发送消息（QUEUE01和QUEUE02都接收）：" + msg);
//        MessageProperties properties = new MessageProperties();
//        properties.setHeader("color", "red");
//        properties.setHeader("speed", "fast");
//        Message message = new Message(msg.getBytes(),properties);
//        rabbitTemplate.convertAndSend("headersExchange", "", message);
//    }
//    public void send07(String msg) {
//        log.info("发送消息（QUEUE01接收）：" + msg);
//        MessageProperties properties = new MessageProperties();
//        properties.setHeader("color", "red");
//        properties.setHeader("speed", "medium");
//        Message message = new Message(msg.getBytes(),properties);
//        rabbitTemplate.convertAndSend("headersExchange", "", message);
//    }
//
//}
