package com.microservice.flowManager.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.flowManager.dto.SubscriptionResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, SubscriptionResponse> redisTemplate(
            RedisConnectionFactory factory, ObjectMapper objectMapper) {
        Jackson2JsonRedisSerializer<SubscriptionResponse> serializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, SubscriptionResponse.class);

        RedisTemplate<String, SubscriptionResponse> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        return template;
    }
}
