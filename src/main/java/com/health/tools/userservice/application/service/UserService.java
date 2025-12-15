package com.health.tools.userservice.application.service;

import com.health.tools.userservice.application.dto.CreateUserRequest;
import com.health.tools.userservice.application.dto.UpdateUserRequest;
import com.health.tools.userservice.application.dto.UserResponse;
import com.health.tools.userservice.domain.model.Email;
import com.health.tools.userservice.domain.model.UserAccount;
import com.health.tools.userservice.domain.model.UserId;
import com.health.tools.userservice.domain.model.UserRole;
import com.health.tools.userservice.domain.port.PasswordEncoderPort;
import com.health.tools.userservice.domain.port.UserRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Application service for user management.
 */
@Service
@Transactional
public class UserService {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public UserService(UserRepositoryPort userRepository, PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Create a new user.
     */
    public UserResponse createUser(CreateUserRequest request) {
        // Check if email already exists
        Email email = Email.of(request.email());
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + request.email());
        }

        // Create user account
        UserId userId = UserId.generate();
        UserAccount user = new UserAccount(userId, email, request.fullName());
        
        // Encode and set password
        String encodedPassword = passwordEncoder.encode(request.password());
        user.setEncodedPassword(encodedPassword);

        // Save user
        userRepository.save(user);
        
        return UserResponse.from(user);
    }

    /**
     * Get user by ID.
     */
    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserById(String id) {
        UserId userId = UserId.of(id);
        return userRepository.findById(userId)
            .map(UserResponse::from);
    }

    /**
     * Get user by email.
     */
    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserByEmail(String email) {
        Email userEmail = Email.of(email);
        return userRepository.findByEmail(userEmail)
            .map(UserResponse::from);
    }

    /**
     * Get all users.
     */
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAllActive().stream()
            .map(UserResponse::from)
            .collect(Collectors.toList());
    }

    /**
     * Update user information.
     */
    public UserResponse updateUser(String id, UpdateUserRequest request) {
        UserId userId = UserId.of(id);
        UserAccount user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

        // Update fields if provided
        if (request.fullName() != null && !request.fullName().isBlank()) {
            // Create new user with updated name (immutable pattern)
            user = new UserAccount(
                user.getId(),
                user.getEmail(),
                request.fullName(),
                user.getEncodedPassword(),
                user.getRoles(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdatedAt()
            );
        }

        // Update roles if provided
        if (request.roles() != null && !request.roles().isEmpty()) {
            // Clear existing roles and add new ones
            Set<UserRole> currentRoles = new HashSet<>(user.getRoles());
            for (UserRole role : currentRoles) {
                user.revokeRole(role);
            }
            for (String roleStr : request.roles()) {
                try {
                    UserRole role = UserRole.valueOf(roleStr.toUpperCase());
                    user.assignRole(role);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid role: " + roleStr);
                }
            }
        }

        if (request.active() != null) {
            if (request.active()) {
                user.activate();
            } else {
                user.deactivate();
            }
        }

        userRepository.save(user);
        return UserResponse.from(user);
    }

    /**
     * Delete user by ID.
     */
    public void deleteUser(String id) {
        UserId userId = UserId.of(id);
        if (!userRepository.findById(userId).isPresent()) {
            throw new IllegalArgumentException("User not found: " + id);
        }
        userRepository.delete(userId);
    }

    /**
     * Check if user exists by email.
     */
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        Email userEmail = Email.of(email);
        return userRepository.findByEmail(userEmail).isPresent();
    }
}
