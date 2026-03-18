package com.oz.controller;

import com.oz.dto.RegisterRequest;
import com.oz.dto.UserProfileDto;
import com.oz.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        UserProfileDto profile = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(profile);
    }

    @GetMapping("/profile/{keycloakId}")
    public ResponseEntity<?> getProfile(@PathVariable String keycloakId) {
        UserProfileDto profile = userService.getUserByKeycloakId(keycloakId);
        return ResponseEntity.ok(profile);
    }

    // Временный endpoint для проверки здоровья
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "user-service"));
    }
}