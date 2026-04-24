package com.microservice.flowManager.client;

import com.microservice.flowManager.dto.SubscriptionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "subscription-service")
public interface SubscriptionClient {
    @GetMapping("/subscriptions/{login}")
    SubscriptionResponse getSubscription(@PathVariable String login);
}
