package com.health.tools.userservice.domain.port;

import com.health.tools.userservice.domain.model.Email;
import com.health.tools.userservice.domain.model.UserAccount;
import com.health.tools.userservice.domain.model.UserId;
import java.util.List;
import java.util.Optional;

/**
 * Puerto (Contrato) para acceder a usuarios en persistencia.
 * Implementa el patrón Ports & Adapters (Hexagonal Architecture).
 * 
 * Los adapters concretos (JPA, MongoDB, etc.) implementarán esta interfaz.
 */
public interface UserRepositoryPort {

    /**
     * Guarda una cuenta de usuario.
     * 
     * @param user cuenta a guardar (puede ser nueva o actualizada)
     */
    void save(UserAccount user);

    /**
     * Busca un usuario por su identificador.
     * 
     * @param id identificador del usuario
     * @return usuario si existe
     */
    Optional<UserAccount> findById(UserId id);

    /**
     * Busca un usuario por correo electrónico.
     * 
     * @param email dirección de correo
     * @return usuario si existe
     */
    Optional<UserAccount> findByEmail(Email email);

    /**
     * Busca todos los usuarios activos.
     * 
     * @return lista de usuarios activos
     */
    List<UserAccount> findAllActive();

    /**
     * Verifica si existe un usuario con el correo indicado.
     * 
     * @param email dirección de correo
     * @return true si existe
     */
    boolean existsByEmail(Email email);

    /**
     * Elimina un usuario.
     * 
     * @param id identificador del usuario a eliminar
     */
    void delete(UserId id);

    /**
     * Obtiene el total de usuarios.
     * 
     * @return cantidad total
     */
    long count();
}
