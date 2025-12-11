package com.health.tools.userservice.application.dto;

import jakarta.validation.constraints.Size;

/**
 * DTO for updating user information.
 */
public record UpdateUserRequest(
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    String fullName,
    
    Boolean active
) {}
