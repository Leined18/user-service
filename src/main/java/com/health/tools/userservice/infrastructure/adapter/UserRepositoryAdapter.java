package com.health.tools.userservice.infrastructure.adapter;

import com.health.tools.userservice.domain.model.Email;
import com.health.tools.userservice.domain.model.UserId;
import com.health.tools.userservice.domain.model.UserAccount;
import com.health.tools.userservice.domain.port.UserRepositoryPort;
import com.health.tools.userservice.infrastructure.entity.UserJpaEntity;
import com.health.tools.userservice.infrastructure.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * UserRepositoryAdapter implements the UserRepositoryPort using Spring Data
 * JPA.
 * Bridges domain layer (port) with Spring persistence framework.
 * 
 * Maintains separation of concerns: domain layer has no Spring dependencies,
 * adapters implement persistence concerns.
 */
@Repository
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserRepositoryJpaPort jpaPort;

    public UserRepositoryAdapter(UserRepositoryJpaPort jpaPort) {
        this.jpaPort = jpaPort;
    }

    @Override
    public void save(UserAccount userAccount) {
        UserJpaEntity entity = UserMapper.toEntity(userAccount);
        jpaPort.save(entity);
    }

    @Override
    public Optional<UserAccount> findById(UserId userId) {
        return jpaPort.findById(userId.value())
                .map(UserMapper::toDomain);
    }

    @Override
    public Optional<UserAccount> findByEmail(Email email) {
        return jpaPort.findByEmail(email.value())
                .map(UserMapper::toDomain);
    }

    @Override
    public List<UserAccount> findAllActive() {
        return jpaPort.findAllByActiveTrue()
                .stream()
                .map(UserMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaPort.existsByEmail(email.value());
    }

    @Override
    public void delete(UserId userId) {
        jpaPort.deleteById(userId.value());
    }

    @Override
    public long count() {
        return jpaPort.count();
    }
}
