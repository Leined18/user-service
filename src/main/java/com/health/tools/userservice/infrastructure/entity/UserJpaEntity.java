package com.health.tools.userservice.infrastructure.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * JPA Entity for User persistence.
 * Maps domain UserAccount aggregate to relational database.
 * 
 * Separates persistence concerns from domain logic,
 * allowing domain layer to remain framework-agnostic.
 */
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uk_users_email", columnNames = "email")
})
public final class UserJpaEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Column(name = "encoded_password", nullable = false)
    private String encodedPassword;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<UserRoleJpaEnum> roles = new HashSet<>();

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    protected UserJpaEntity() {
        // JPA default constructor
    }

    public UserJpaEntity(String id, String email, String fullName,
            String encodedPassword, Set<UserRoleJpaEnum> roles,
            Boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.encodedPassword = encodedPassword;
        this.roles = new HashSet<>(roles);
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public Set<UserRoleJpaEnum> getRoles() {
        return new HashSet<>(roles);
    }

    public Boolean getActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters for updates
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public void setRoles(Set<UserRoleJpaEnum> roles) {
        this.roles = new HashSet<>(roles);
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
