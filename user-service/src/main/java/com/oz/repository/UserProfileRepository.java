package com.oz.repository;

import com.oz.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByKeycloakId(String keycloakId);
    Optional<UserProfile> findByEmail(String email);
    boolean existsByEmail(String email);
}