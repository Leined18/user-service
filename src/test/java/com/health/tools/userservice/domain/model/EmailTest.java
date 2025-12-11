package com.health.tools.userservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    @DisplayName("Create valid Email")
    void createsValidEmail() {
        Email email = Email.of("user@example.com");

        assertEquals("user@example.com", email.value());
    }

    @Test
    @DisplayName("Normalizes email to lowercase")
    void normalizesToLowercase() {
        Email email = Email.of("USER@EXAMPLE.COM");

        assertEquals("user@example.com", email.value());
    }

    @Test
    @DisplayName("Trims whitespace")
    void trimsWhitespace() {
        Email email = Email.of("  user@example.com  ");

        assertEquals("user@example.com", email.value());
    }

    @Test
    @DisplayName("Rejects email without @")
    void rejectsInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> Email.of("notanemail"));
    }

    @Test
    @DisplayName("Rejects null email")
    void rejectsNull() {
        assertThrows(NullPointerException.class, () -> Email.of(null));
    }

    @Test
    @DisplayName("Rejects empty email")
    void rejectsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> Email.of("   "));
    }

    @Test
    @DisplayName("Two emails with same value are equal")
    void testEquality() {
        Email email1 = Email.of("user@example.com");
        Email email2 = Email.of("USER@EXAMPLE.COM");

        assertEquals(email1, email2);
    }
}
