package com.ymt.seckill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置类：主要作用为实现序列化
 */
@Configuration
public class RedisConfig {
    // key为String类型，value为存储的对象
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 配置redisTemplate
        // key序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // value序列化
        // jdk产生的是二进制；而jackson产生的是字符串，需要传入类对象；这里的Generic是通用的json转换序列化之后也是一个json数据，并不需要传类对象
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // Redis中还有一种特殊一点的数据结构——Hash类型
        // Hash类型 key序列化
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        // Hash类型 value序列化
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        // 注入连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
}
