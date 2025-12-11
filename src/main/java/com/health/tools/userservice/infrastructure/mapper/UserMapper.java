package com.health.tools.userservice.infrastructure.mapper;

import com.health.tools.userservice.domain.model.Email;
import com.health.tools.userservice.domain.model.UserId;
import com.health.tools.userservice.domain.model.UserAccount;
import com.health.tools.userservice.domain.model.UserRole;
import com.health.tools.userservice.infrastructure.entity.UserJpaEntity;
import com.health.tools.userservice.infrastructure.entity.UserRoleJpaEnum;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for converting between domain UserAccount aggregate and JPA
 * UserJpaEntity.
 * Handles conversion in both directions to maintain separation of concerns.
 */
public final class UserMapper {

    private UserMapper() {
        // Utility class
    }

    /**
     * Convert JPA entity to domain aggregate.
     */
    public static UserAccount toDomain(UserJpaEntity entity) {
        UserId userId = UserId.of(entity.getId());
        Email email = Email.of(entity.getEmail());

        var roles = entity.getRoles().stream()
                .map(roleEnum -> UserRole.valueOf(roleEnum.name()))
                .collect(Collectors.toSet());

        UserAccount account = new UserAccount(
                userId,
                email,
                entity.getFullName(),
                entity.getEncodedPassword(),
                roles,
                entity.getActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());

        return account;
    }

    /**
     * Convert domain aggregate to JPA entity.
     */
    public static UserJpaEntity toEntity(UserAccount account) {
        var roles = account.getRoles().stream()
                .map(role -> UserRoleJpaEnum.valueOf(role.name()))
                .collect(Collectors.toSet());

        return new UserJpaEntity(
                account.getId().value(),
                account.getEmail().value(),
                account.getFullName(),
                account.getEncodedPassword(),
                roles,
                account.isActive(),
                account.getCreatedAt(),
                account.getUpdatedAt());
    }
}
