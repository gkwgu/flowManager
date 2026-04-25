package com.microservice.flowManager.kafka;

import com.microservice.flowManager.service.SubscriptionCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionCacheConsumer {

    private final SubscriptionCacheService subscriptionCacheService;

    @KafkaListener(topics = "subscription-expired-topic", groupId = "flow-manager-cache-group")
    public void handleCacheInvalidation(String login) {
        log.info("Received cache invalidation for: {}", login);
        subscriptionCacheService.evictCache(login);
    }
}