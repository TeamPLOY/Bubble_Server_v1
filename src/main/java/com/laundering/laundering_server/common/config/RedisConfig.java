package com.laundering.laundering_server.common.config;

import com.laundering.laundering_server.domain.washingMachine.model.dto.response.WashingMachineResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, List<WashingMachineResponse>> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, List<WashingMachineResponse>> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}