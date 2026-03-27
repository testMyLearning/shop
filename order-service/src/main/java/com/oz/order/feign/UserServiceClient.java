package com.oz.order.feign;

import com.oz.common.dto.UserProfileDto;
import com.oz.order.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://user-service:8081",configuration = FeignConfig.class)
public interface UserServiceClient {
    @GetMapping("/api/users/profile/{keycloakId}")
    UserProfileDto getUserById(@PathVariable("keycloakId") String keycloakId);
}
