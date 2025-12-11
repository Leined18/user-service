package com.health.tools.userservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserAccountTest {

    @Test
    @DisplayName("Create new user account")
    void createsNewAccount() {
        UserId userId = UserId.generate();
        Email email = Email.of("user@example.com");
        UserAccount user = new UserAccount(userId, email, "John Doe");

        assertEquals(userId, user.getId());
        assertEquals(email, user.getEmail());
        assertEquals("John Doe", user.getFullName());
        assertTrue(user.isActive());
        assertNotNull(user.getCreatedAt());
    }

    @Test
    @DisplayName("Rejects null user ID")
    void rejectsNullUserId() {
        Email email = Email.of("user@example.com");
        assertThrows(NullPointerException.class, () -> new UserAccount(null, email, "Name"));
    }

    @Test
    @DisplayName("Rejects null email")
    void rejectsNullEmail() {
        UserId userId = UserId.generate();
        assertThrows(NullPointerException.class, () -> new UserAccount(userId, null, "Name"));
    }

    @Test
    @DisplayName("Rejects null or empty full name")
    void rejectsNullFullName() {
        UserId userId = UserId.generate();
        Email email = Email.of("user@example.com");

        assertThrows(NullPointerException.class, () -> new UserAccount(userId, email, null));
        assertThrows(IllegalArgumentException.class, () -> new UserAccount(userId, email, "   "));
    }

    @Test
    @DisplayName("Assign and revoke roles")
    void managesRoles() {
        UserAccount user = new UserAccount(UserId.generate(), Email.of("user@example.com"), "John");

        user.assignRole(UserRole.THERAPIST);
        assertTrue(user.hasRole(UserRole.THERAPIST));

        user.assignRole(UserRole.ADMIN);
        assertTrue(user.hasRole(UserRole.ADMIN));

        user.revokeRole(UserRole.THERAPIST);
        assertFalse(user.hasRole(UserRole.THERAPIST));
        assertTrue(user.hasRole(UserRole.ADMIN));
    }

    @Test
    @DisplayName("Activate and deactivate account")
    void managesActiveStatus() {
        UserAccount user = new UserAccount(UserId.generate(), Email.of("user@example.com"), "John");

        assertTrue(user.isActive());
        user.deactivate();
        assertFalse(user.isActive());
        user.activate();
        assertTrue(user.isActive());
    }

    @Test
    @DisplayName("Set encoded password")
    void setsEncodedPassword() {
        UserAccount user = new UserAccount(UserId.generate(), Email.of("user@example.com"), "John");

        assertNull(user.getEncodedPassword());
        user.setEncodedPassword("$2a$10$encodedPassword");
        assertEquals("$2a$10$encodedPassword", user.getEncodedPassword());
    }

    @Test
    @DisplayName("Two accounts with same ID are equal")
    void testEquality() {
        UserId userId = UserId.of("user-123");
        Email email1 = Email.of("user1@example.com");
        Email email2 = Email.of("user2@example.com");

        UserAccount account1 = new UserAccount(userId, email1, "John");
        UserAccount account2 = new UserAccount(userId, email2, "Jane");

        assertEquals(account1, account2);
    }
}
