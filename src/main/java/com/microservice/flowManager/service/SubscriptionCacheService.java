package com.microservice.flowManager.service;

import com.microservice.flowManager.client.SubscriptionClient;
import com.microservice.flowManager.dto.SubscriptionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionCacheService {

    private final SubscriptionClient subscriptionClient;

    @Cacheable(value = "subscription", key = "#userLogin")
    public SubscriptionResponse getSubscription(String userLogin) {
        log.info("Cache miss, fetching subscription for {}", userLogin);
        return subscriptionClient.getSubscription(userLogin);
    }

    @CacheEvict(value = "subscription", key = "#userLogin")
    public void evictCache(String userLogin) {
        log.info("Cache evicted for {}", userLogin);
    }
}