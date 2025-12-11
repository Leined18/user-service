package com.health.tools.userservice.domain.model;

/**
 * Enumeración de roles de usuario en la plataforma.
 * Extensible: agregar nuevos roles sin modificar código existente.
 */
public enum UserRole {
    ADMIN("ADMIN", "Administrador del sistema"),
    THERAPIST("THERAPIST", "Terapeuta"),
    CLIENT("CLIENT", "Cliente/Paciente"),
    VIEWER("VIEWER", "Solo lectura");

    private final String code;
    private final String label;

    UserRole(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String code() {
        return code;
    }

    public String label() {
        return label;
    }

    public static UserRole fromCode(String code) {
        for (UserRole role : values()) {
            if (role.code.equals(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Rol desconocido: " + code);
    }
}
