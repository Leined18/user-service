package com.health.tools.userservice.domain.model;

import com.health.tools.common.domain.ValueObject;
import java.util.Objects;
import java.util.UUID;

/**
 * Value Object que representa el identificador único de un usuario.
 * Inmutable, sin identidad propia, comparable por valor.
 */
public final class UserId implements ValueObject {
    private final String value;

    private UserId(String value) {
        this.value = Objects.requireNonNull(value, "UserId cannot be null");
    }

    /**
     * Crea un UserId a partir de un string.
     * 
     * @param value identificador en formato string
     * @return nuevo UserId
     */
    public static UserId of(String value) {
        return new UserId(value);
    }

    /**
     * Genera un UserId aleatorio usando UUID.
     * 
     * @return nuevo UserId con UUID generado
     */
    public static UserId generate() {
        return new UserId(UUID.randomUUID().toString());
    }

    /**
     * Retorna el valor del identificador.
     * 
     * @return string del ID
     */
    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserId userId))
            return false;
        return Objects.equals(value, userId.value);
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
