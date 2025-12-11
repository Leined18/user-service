package com.health.tools.userservice.infrastructure.entity;

/**
 * JPA Enum for User roles in database.
 * Mirrors domain UserRole enumeration for persistence.
 */
public enum UserRoleJpaEnum {
    ADMIN,
    THERAPIST,
    CLIENT,
    VIEWER
}
