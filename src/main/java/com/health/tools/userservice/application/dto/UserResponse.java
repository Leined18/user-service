package com.health.tools.userservice.application.dto;

import com.health.tools.userservice.domain.model.UserAccount;
import com.health.tools.userservice.domain.model.UserRole;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for user response.
 */
public record UserResponse(
    String id,
    String email,
    String fullName,
    Set<UserRole> roles,
    boolean active,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static UserResponse from(UserAccount user) {
        return new UserResponse(
            user.getId().value(),
            user.getEmail().value(),
            user.getFullName(),
            user.getRoles(),
            user.isActive(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}
