package com.health.tools.userservice.infrastructure.adapter;

import com.health.tools.userservice.domain.port.PasswordEncoderPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests for PasswordEncoderAdapter.
 * Verifies correct delegation to Spring Security's BCryptPasswordEncoder.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PasswordEncoderAdapter Tests")
class PasswordEncoderAdapterTest {

    @Mock
    private PasswordEncoder springPasswordEncoder;

    @InjectMocks
    private PasswordEncoderAdapter adapter;

    @BeforeEach
    void setUp() {
        // Setup default behavior
    }

    @Test
    @DisplayName("should encode password using Spring's PasswordEncoder")
    void testEncode() {
        String rawPassword = "myPassword123";
        String encodedPassword = "$2a$10$encoded";

        when(springPasswordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        String result = adapter.encode(rawPassword);

        assertEquals(encodedPassword, result);
        verify(springPasswordEncoder, times(1)).encode(rawPassword);
    }

    @Test
    @DisplayName("should match password with encoded password")
    void testMatches() {
        String rawPassword = "myPassword123";
        String encodedPassword = "$2a$10$encoded";

        when(springPasswordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        boolean matches = adapter.matches(rawPassword, encodedPassword);

        assertTrue(matches);
        verify(springPasswordEncoder, times(1)).matches(rawPassword, encodedPassword);
    }

    @Test
    @DisplayName("should return false when password does not match")
    void testMatchesReturnsFalse() {
        String rawPassword = "wrongPassword";
        String encodedPassword = "$2a$10$encoded";

        when(springPasswordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        boolean matches = adapter.matches(rawPassword, encodedPassword);

        assertFalse(matches);
        verify(springPasswordEncoder, times(1)).matches(rawPassword, encodedPassword);
    }

    @Test
    @DisplayName("should handle null password gracefully")
    void testEncodesNullPassword() {
        when(springPasswordEncoder.encode(null)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> adapter.encode(null));
        verify(springPasswordEncoder, times(1)).encode(null);
    }

    @Test
    @DisplayName("should implement PasswordEncoderPort interface")
    void testImplementsPort() {
        assertTrue(adapter instanceof PasswordEncoderPort);
    }
}
