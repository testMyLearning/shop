
package com.oz.service;

import com.oz.dto.RegisterRequest;
import com.oz.common.dto.UserProfileDto;
import com.oz.entity.UserProfile;
import com.oz.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserProfileRepository userProfileRepository;
    private final KeycloakAdminService keycloakAdminService;

    @Transactional
    public UserProfileDto registerUser(RegisterRequest request) {
        // 1. Проверяем, нет ли уже такого email в нашей БД
        if (userProfileRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User with this email already exists in our database");
        }

        // 2. Проверяем, нет ли уже такого email в Keycloak
        String existingUserId = keycloakAdminService.findUserIdByEmail(request.getEmail());
        if (existingUserId != null) {
            log.warn("User with email {} already exists in Keycloak with ID: {}",
                    request.getEmail(), existingUserId);
            throw new RuntimeException("User with this email already exists in authentication system");
        }

        // 3. Создаем пользователя в Keycloak (admin token получается автоматически внутри библиотеки!)
        String keycloakId;
        try {
            keycloakId = keycloakAdminService.createUser(request);
            log.info("User created in Keycloak with ID: {}", keycloakId);
        } catch (Exception e) {
            log.error("Failed to create user in Keycloak", e);
            throw new RuntimeException("Registration failed: unable to create user in authentication system", e);
        }

        // 4. Назначаем роль buyer (исправлено - убрали adminToken)
//        try {
//            keycloakAdminService.assignRole(keycloakId, );
//            log.info("Role 'buyer' assigned to user {}", keycloakId);
//        } catch (Exception e) {
//            log.error("Failed to assign role to user {}, but user was created", keycloakId, e);
//            // Важно: пользователь уже создан, но роль не назначена
//            // Можно попробовать позже или уведомить администратора
//        }

        // 5. Сохраняем профиль в своей БД
        UserProfile profile = new UserProfile();
        profile.setKeycloakId(keycloakId);
        profile.setEmail(request.getEmail());
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setPhone(request.getPhone());
        profile.setAddress(request.getAddress());

        UserProfile savedProfile = userProfileRepository.save(profile);

        log.info("User registered successfully: {} with Keycloak ID: {}",
                request.getEmail(), keycloakId);

        return mapToDto(savedProfile);
    }

    public UserProfileDto getUserByKeycloakId(String keycloakId) {
        UserProfile profile = userProfileRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found with Keycloak ID: " + keycloakId));
        return mapToDto(profile);
    }

    public UserProfileDto getUserByEmail(String email) {
        UserProfile profile = userProfileRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return mapToDto(profile);
    }

    private UserProfileDto mapToDto(UserProfile profile) {
        UserProfileDto dto = new UserProfileDto();
        dto.setKeycloakId(profile.getKeycloakId());
        dto.setEmail(profile.getEmail());
        dto.setFirstName(profile.getFirstName());
        dto.setLastName(profile.getLastName());
        dto.setPhone(profile.getPhone());
        dto.setAddress(profile.getAddress());
        dto.setCreatedAt(profile.getCreatedAt());
        return dto;
    }

    public UserProfileDto getUser(Long userId) {
        UserProfile profile = userProfileRepository.findById(userId).orElseThrow();
        return this.mapToDto(profile);
    }
}