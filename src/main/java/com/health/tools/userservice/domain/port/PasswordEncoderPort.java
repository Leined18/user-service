package com.health.tools.userservice.domain.port;

/**
 * Puerto para abstracción de codificación de contraseñas.
 * Permite inyectar diferentes estrategias (BCrypt, PBKDF2, Argon2, etc.)
 */
public interface PasswordEncoderPort {

    /**
     * Codifica una contraseña en texto plano.
     * 
     * @param rawPassword contraseña sin codificar
     * @return contraseña codificada
     */
    String encode(String rawPassword);

    /**
     * Verifica si una contraseña en texto plano coincide con una codificada.
     * 
     * @param rawPassword     contraseña sin codificar
     * @param encodedPassword contraseña codificada
     * @return true si coinciden
     */
    boolean matches(String rawPassword, String encodedPassword);
}
