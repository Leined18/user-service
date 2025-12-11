package com.health.tools.userservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserIdTest {

    @Test
    @DisplayName("Create UserId from string")
    void createsFromString() {
        UserId userId = UserId.of("user-123");

        assertEquals("user-123", userId.value());
    }

    @Test
    @DisplayName("Generate random UserId")
    void generatesRandomId() {
        UserId id1 = UserId.generate();
        UserId id2 = UserId.generate();

        assertNotNull(id1.value());
        assertNotNull(id2.value());
        assertNotEquals(id1, id2);
    }

    @Test
    @DisplayName("Two UserIds with same value are equal")
    void testEquality() {
        UserId id1 = UserId.of("user-123");
        UserId id2 = UserId.of("user-123");

        assertEquals(id1, id2);
    }

    @Test
    @DisplayName("Rejects null value")
    void rejectsNull() {
        assertThrows(NullPointerException.class, () -> UserId.of(null));
    }
}
