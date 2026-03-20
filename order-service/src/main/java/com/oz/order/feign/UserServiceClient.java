package com.oz.order.feign;

import com.oz.common.dto.UserProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("user-service")
public interface UserServiceClient {
    @GetMapping("/api/users/profile/{keycloakId}")
    UserProfileDto getUserById(@PathVariable("id") String keycloakId);
}
