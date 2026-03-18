package com.oz.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oz.config.KeycloakConfig;
import com.oz.dto.RegisterRequest;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeycloakAdminService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    /**
     * Создать пользователя в Keycloak
     * @return keycloakId созданного пользователя
     */
    public String createUser(RegisterRequest request) {
        try {
            // 1. Создаем представление пользователя
            UserRepresentation user = new UserRepresentation();
            user.setUsername(request.getEmail());
            user.setEmail(request.getEmail());
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEnabled(true);
            user.setEmailVerified(false);

            // 2. Создаем учетные данные (пароль)
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(request.getPassword());
            credential.setTemporary(false);  // не временный пароль [citation:10]

            user.setCredentials(Collections.singletonList(credential));

            // 3. Получаем ресурс пользователей для нашего realm
            UsersResource usersResource = keycloak.realm(realm).users();

            // 4. Создаем пользователя
            Response response = usersResource.create(user);

            if (response.getStatus() == 201) {  // Created
                // Получаем ID из Location header
                String location = response.getLocation().getPath();
                String userId = location.substring(location.lastIndexOf('/') + 1);
                if(request.getRole()!=null && !request.getRole().isBlank()) {
                    this.assignRole(userId,request.getRole());
                }else{
                    this.assignRole(userId,"buyer");
                }
                log.info("User created in Keycloak with ID: {}", userId);
                return userId;
            } else {
                String error = response.readEntity(String.class);
                log.error("Failed to create user in Keycloak: {}", error);
                throw new RuntimeException("Keycloak creation failed: " + response.getStatusInfo());
            }

        } catch (Exception e) {
            log.error("Exception during Keycloak user creation", e);
            throw new RuntimeException("Failed to create user in Keycloak", e);
        }
    }

    /**
     * Назначить роль пользователю
     */
    public void assignRole(String userId, String roleName) {
        try {
            // Поиск роли и назначение пользователю
            var role = keycloak.realm(realm)
                    .roles()
                    .get(roleName)
                    .toRepresentation();

            keycloak.realm(realm)
                    .users()
                    .get(userId)
                    .roles()
                    .realmLevel()
                    .add(Collections.singletonList(role));

            log.info("Role {} assigned to user {}", roleName, userId);

        } catch (Exception e) {
            log.error("Failed to assign role {} to user {}", roleName, userId, e);
            throw new RuntimeException("Failed to assign role", e);
        }
    }

    /**
     * Найти пользователя по email
     */
    public String findUserIdByEmail(String email) {
        var users = keycloak.realm(realm)
                .users()
                .searchByEmail(email, true);  // точное совпадение [citation:7]

        if (users.isEmpty()) {
            return null;
        }
        return users.get(0).getId();
    }
}