package com.health.tools.userservice.domain.model;

import com.health.tools.common.domain.ValueObject;
import java.util.Objects;

/**
 * Value Object que representa una dirección de correo electrónico.
 * Valida el formato básico y es inmutable.
 */
public final class Email implements ValueObject {
    private final String value;

    private Email(String value) {
        String trimmed = Objects.requireNonNull(value, "Email cannot be null").trim().toLowerCase();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!trimmed.contains("@")) {
            throw new IllegalArgumentException("Email must contain @ symbol");
        }
        this.value = trimmed;
    }

    /**
     * Crea un Email a partir de un string.
     * 
     * @param value dirección de correo
     * @return nuevo Email
     * @throws IllegalArgumentException si el formato es inválido
     */
    public static Email of(String value) {
        return new Email(value);
    }

    /**
     * Retorna el valor del correo.
     * 
     * @return string del correo
     */
    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Email email))
            return false;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
