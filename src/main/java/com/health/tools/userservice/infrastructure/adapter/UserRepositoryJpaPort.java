package com.health.tools.userservice.infrastructure.adapter;

import com.health.tools.userservice.infrastructure.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository interface for UserJpaEntity.
 * Provides persistence operations abstraction for UserRepositoryAdapter.
 * 
 * Not exposed to domain layer - domain uses UserRepositoryPort interface
 * instead.
 * This is infrastructure-specific implementation detail.
 */
public interface UserRepositoryJpaPort extends JpaRepository<UserJpaEntity, String> {

    Optional<UserJpaEntity> findByEmail(String email);

    List<UserJpaEntity> findAllByActiveTrue();

    boolean existsByEmail(String email);
}
