package com.health.tools.userservice.infrastructure.mapper;

import com.health.tools.userservice.domain.model.Email;
import com.health.tools.userservice.domain.model.UserId;
import com.health.tools.userservice.domain.model.UserAccount;
import com.health.tools.userservice.domain.model.UserRole;
import com.health.tools.userservice.infrastructure.entity.UserJpaEntity;
import com.health.tools.userservice.infrastructure.entity.UserRoleJpaEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for UserMapper.
 * Verifies correct conversion between domain and JPA entity layers.
 */
@DisplayName("UserMapper Tests")
class UserMapperTest {

    private UserAccount domainUser;
    private UserJpaEntity jpaEntity;

    @BeforeEach
    void setUp() {
        UserId userId = UserId.generate();
        Email email = Email.of("user@example.com");
        LocalDateTime now = LocalDateTime.now();

        domainUser = new UserAccount(userId, email, "Jane Smith");
        domainUser.setEncodedPassword("encoded_pwd");
        domainUser.assignRole(UserRole.THERAPIST);
        domainUser.assignRole(UserRole.ADMIN);

        jpaEntity = new UserJpaEntity(
                userId.value(),
                email.value(),
                "Jane Smith",
                "encoded_pwd",
                Set.of(UserRoleJpaEnum.THERAPIST, UserRoleJpaEnum.ADMIN),
                true,
                now,
                now);
    }

    @Test
    @DisplayName("should convert domain user to JPA entity")
    void testToEntity() {
        UserJpaEntity result = UserMapper.toEntity(domainUser);

        assertEquals(domainUser.getId().value(), result.getId());
        assertEquals(domainUser.getEmail().value(), result.getEmail());
        assertEquals(domainUser.getFullName(), result.getFullName());
        assertEquals(domainUser.getEncodedPassword(), result.getEncodedPassword());
        assertEquals(2, result.getRoles().size());
        assertTrue(result.getActive());
    }

    @Test
    @DisplayName("should convert JPA entity to domain user")
    void testToDomain() {
        UserAccount result = UserMapper.toDomain(jpaEntity);

        assertEquals(jpaEntity.getId(), result.getId().value());
        assertEquals(jpaEntity.getEmail(), result.getEmail().value());
        assertEquals(jpaEntity.getFullName(), result.getFullName());
        assertEquals(jpaEntity.getEncodedPassword(), result.getEncodedPassword());
        assertEquals(2, result.getRoles().size());
        assertTrue(result.isActive());
    }

    @Test
    @DisplayName("should preserve roles when converting to entity")
    void testRolesPreservedToEntity() {
        domainUser.revokeRole(UserRole.ADMIN);

        UserJpaEntity result = UserMapper.toEntity(domainUser);

        assertEquals(1, result.getRoles().size());
        assertTrue(result.getRoles().contains(UserRoleJpaEnum.THERAPIST));
    }

    @Test
    @DisplayName("should preserve roles when converting to domain")
    void testRolesPreservedToDomain() {
        UserAccount result = UserMapper.toDomain(jpaEntity);

        assertEquals(2, result.getRoles().size());
        assertTrue(result.hasRole(UserRole.THERAPIST));
        assertTrue(result.hasRole(UserRole.ADMIN));
    }

    @Test
    @DisplayName("should preserve inactive status when converting to entity")
    void testInactiveStatusToEntity() {
        domainUser.deactivate();

        UserJpaEntity result = UserMapper.toEntity(domainUser);

        assertFalse(result.getActive());
    }

    @Test
    @DisplayName("should preserve inactive status when converting to domain")
    void testInactiveStatusToDomain() {
        jpaEntity.setActive(false);

        UserAccount result = UserMapper.toDomain(jpaEntity);

        assertFalse(result.isActive());
    }

    @Test
    @DisplayName("should handle empty roles correctly")
    void testEmptyRoles() {
        UserJpaEntity entityNoRoles = new UserJpaEntity(
                jpaEntity.getId(),
                jpaEntity.getEmail(),
                jpaEntity.getFullName(),
                jpaEntity.getEncodedPassword(),
                Set.of(),
                true,
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt());

        UserAccount result = UserMapper.toDomain(entityNoRoles);

        assertTrue(result.getRoles().isEmpty());
    }

    @Test
    @DisplayName("should round-trip convert domain to entity and back")
    void testRoundTripConversion() {
        UserJpaEntity entity = UserMapper.toEntity(domainUser);
        UserAccount result = UserMapper.toDomain(entity);

        assertEquals(domainUser.getId().value(), result.getId().value());
        assertEquals(domainUser.getEmail().value(), result.getEmail().value());
        assertEquals(domainUser.getFullName(), result.getFullName());
        assertEquals(domainUser.getRoles().size(), result.getRoles().size());
    }
}
