package com.health.tools.userservice.domain.model;

import com.health.tools.common.domain.AggregateRoot;
import com.health.tools.common.domain.DomainEvent;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Agregado raíz para cuenta de usuario.
 * Representa un usuario en el sistema con permisos y estado.
 * 
 * Invariantes:
 * - userId no puede ser nulo
 * - email no puede ser nulo
 * - fullName no puede ser nulo o vacío
 * - Un usuario puede tener múltiples roles
 */
public final class UserAccount implements AggregateRoot {
    private UserId id;
    private Email email;
    private String fullName;
    private String encodedPassword;
    private Set<UserRole> roles;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Crea una nueva cuenta de usuario.
     * 
     * @param id       identificador único del usuario
     * @param email    dirección de correo
     * @param fullName nombre completo
     */
    public UserAccount(UserId id, Email email, String fullName) {
        this.id = Objects.requireNonNull(id, "UserId cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.fullName = Objects.requireNonNull(fullName, "FullName cannot be null").trim();
        if (this.fullName.isEmpty()) {
            throw new IllegalArgumentException("FullName cannot be empty");
        }
        this.roles = new HashSet<>();
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Constructor para carga desde persistencia.
     */
    public UserAccount(UserId id, Email email, String fullName, String encodedPassword,
            Set<UserRole> roles, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.encodedPassword = encodedPassword;
        this.roles = roles != null ? new HashSet<>(roles) : new HashSet<>();
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ============= AGGREGATE INTERFACE =============

    @Override
    public List<DomainEvent> domainEvents() {
        return Collections.emptyList();
    }

    @Override
    public void clearDomainEvents() {
        // No events currently implemented
    }

    // ============= BUSINESS METHODS =============

    /**
     * Asigna una contraseña (ya codificada) al usuario.
     * 
     * @param encodedPassword contraseña codificada
     */
    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Asigna un rol al usuario.
     * 
     * @param role rol a asignar
     */
    public void assignRole(UserRole role) {
        Objects.requireNonNull(role, "Role cannot be null");
        roles.add(role);
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Revoca un rol del usuario.
     * 
     * @param role rol a revocar
     */
    public void revokeRole(UserRole role) {
        roles.remove(role);
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Verifica si el usuario tiene un rol específico.
     * 
     * @param role rol a verificar
     * @return true si el usuario tiene el rol
     */
    public boolean hasRole(UserRole role) {
        return roles.contains(role);
    }

    /**
     * Desactiva la cuenta del usuario.
     */
    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Activa la cuenta del usuario.
     */
    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }

    // ============= GETTERS =============

    public UserId getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public Set<UserRole> getRoles() {
        return Set.copyOf(roles);
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserAccount account))
            return false;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "id=" + id +
                ", email=" + email +
                ", fullName='" + fullName + '\'' +
                ", active=" + active +
                ", roles=" + roles +
                '}';
    }
}
