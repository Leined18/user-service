package com.health.tools.userservice.infrastructure.adapter;

import com.health.tools.userservice.domain.model.Email;
import com.health.tools.userservice.domain.model.UserId;
import com.health.tools.userservice.domain.model.UserAccount;
import com.health.tools.userservice.domain.model.UserRole;
import com.health.tools.userservice.infrastructure.entity.UserJpaEntity;
import com.health.tools.userservice.infrastructure.entity.UserRoleJpaEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests for UserRepositoryAdapter.
 * Verifies correct translation between domain and JPA entity layers.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserRepositoryAdapter Tests")
class UserRepositoryAdapterTest {

    @Mock
    private UserRepositoryJpaPort jpaPort;

    @InjectMocks
    private UserRepositoryAdapter adapter;

    private UserAccount domainUser;
    private UserJpaEntity jpaEntity;

    @BeforeEach
    void setUp() {
        UserId userId = UserId.generate();
        Email email = Email.of("test@example.com");
        domainUser = new UserAccount(userId, email, "John Doe");
        domainUser.setEncodedPassword("encoded_password");
        domainUser.assignRole(UserRole.CLIENT);

        jpaEntity = new UserJpaEntity(
                userId.value(),
                email.value(),
                "John Doe",
                "encoded_password",
                Set.of(UserRoleJpaEnum.CLIENT),
                true,
                LocalDateTime.now(),
                LocalDateTime.now());
    }

    @Test
    @DisplayName("should save user to database")
    void testSave() {
        adapter.save(domainUser);

        verify(jpaPort, times(1)).save(any(UserJpaEntity.class));
    }

    @Test
    @DisplayName("should find user by ID")
    void testFindById() {
        when(jpaPort.findById(anyString())).thenReturn(Optional.of(jpaEntity));

        Optional<UserAccount> result = adapter.findById(domainUser.getId());

        assertTrue(result.isPresent());
        assertEquals(domainUser.getId().value(), result.get().getId().value());
        verify(jpaPort, times(1)).findById(anyString());
    }

    @Test
    @DisplayName("should find user by email")
    void testFindByEmail() {
        when(jpaPort.findByEmail(anyString())).thenReturn(Optional.of(jpaEntity));

        Optional<UserAccount> result = adapter.findByEmail(domainUser.getEmail());

        assertTrue(result.isPresent());
        assertEquals(domainUser.getEmail().value(), result.get().getEmail().value());
        verify(jpaPort, times(1)).findByEmail(anyString());
    }

    @Test
    @DisplayName("should find all active users")
    void testFindAllActive() {
        when(jpaPort.findAllByActiveTrue()).thenReturn(List.of(jpaEntity));

        List<UserAccount> results = adapter.findAllActive();

        assertEquals(1, results.size());
        assertEquals(domainUser.getId().value(), results.get(0).getId().value());
        verify(jpaPort, times(1)).findAllByActiveTrue();
    }

    @Test
    @DisplayName("should check if email exists")
    void testExistsByEmail() {
        when(jpaPort.existsByEmail(anyString())).thenReturn(true);

        boolean exists = adapter.existsByEmail(domainUser.getEmail());

        assertTrue(exists);
        verify(jpaPort, times(1)).existsByEmail(anyString());
    }

    @Test
    @DisplayName("should delete user by ID")
    void testDelete() {
        adapter.delete(domainUser.getId());

        verify(jpaPort, times(1)).deleteById(anyString());
    }

    @Test
    @DisplayName("should count total users")
    void testCount() {
        when(jpaPort.count()).thenReturn(5L);

        long count = adapter.count();

        assertEquals(5L, count);
        verify(jpaPort, times(1)).count();
    }

    @Test
    @DisplayName("should return empty optional when user not found")
    void testFindByIdNotFound() {
        when(jpaPort.findById(anyString())).thenReturn(Optional.empty());

        Optional<UserAccount> result = adapter.findById(domainUser.getId());

        assertTrue(result.isEmpty());
        verify(jpaPort, times(1)).findById(anyString());
    }

    @Test
    @DisplayName("should return empty list when no active users exist")
    void testFindAllActiveEmpty() {
        when(jpaPort.findAllByActiveTrue()).thenReturn(List.of());

        List<UserAccount> results = adapter.findAllActive();

        assertTrue(results.isEmpty());
        verify(jpaPort, times(1)).findAllByActiveTrue();
    }
}
