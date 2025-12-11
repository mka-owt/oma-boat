package com.omaden.boat.boats.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Boat Domain Model Tests")
class BoatTest {

    @Test
    @DisplayName("Should create a new boat with valid name and description")
    void shouldCreateBoatWithValidData() {
        // Given
        String name = "Titanic";
        String description = "Titanic Description";

        // When
        Boat boat = Boat.create(name, description);

        // Then
        assertNotNull(boat);
        assertNull(boat.getId());
        assertEquals("Titanic", boat.getName());
        assertEquals("Titanic Description", boat.getDescription());
    }

    @Test
    @DisplayName("Should trim whitespace from name and description")
    void shouldTrimWhitespace() {
        // Given
        String name = "  Titanic  ";
        String description = "  Titanic Description  ";

        // When
        Boat boat = Boat.create(name, description);

        // Then
        assertEquals("Titanic", boat.getName());
        assertEquals("Titanic Description", boat.getDescription());
    }

    @Test
    @DisplayName("Should throw exception when name is null")
    void shouldThrowExceptionWhenNameIsNull() {
        // When & Then
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class, () -> Boat.create(null, "Description"));
        assertEquals("Boat name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when name is empty")
    void shouldThrowExceptionWhenNameIsEmpty() {
        // When & Then
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> Boat.create("", "Description"));
        assertEquals("Boat name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when name exceeds 100 characters")
    void shouldThrowExceptionWhenNameExceedsMaxLength() {
        // Given
        String longName = "a".repeat(101);

        // When & Then
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class, () -> Boat.create(longName, "Description"));
        assertEquals("Boat name cannot exceed 100 characters", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when description is null")
    void shouldThrowExceptionWhenDescriptionIsNull() {
        // When & Then
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> Boat.create("Name", null));
        assertEquals("Boat description cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when description is empty")
    void shouldThrowExceptionWhenDescriptionIsEmpty() {
        // When & Then
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> Boat.create("Name", ""));
        assertEquals("Boat description cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when description exceeds 500 characters")
    void shouldThrowExceptionWhenDescriptionExceedsMaxLength() {
        // Given
        String longDescription = "a".repeat(501);

        // When & Then
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class, () -> Boat.create("Name", longDescription));
        assertEquals("Boat description cannot exceed 500 characters", exception.getMessage());
    }

    @Test
    @DisplayName("Should update boat name and description")
    void shouldUpdateBoat() {
        // Given
        Boat boat = Boat.create("Titanic", "Old description");

        // When
        boat.update("Any Boat", "New description");

        // Then
        assertEquals("Any Boat", boat.getName());
        assertEquals("New description", boat.getDescription());
    }

    @Test
    @DisplayName("Should assign id to boat")
    void shouldAssignId() {
        // Given
        Boat boat = Boat.create("Titanic", "Description");

        // When
        boat.assignId(1L);

        // Then
        assertEquals(1L, boat.getId());
    }
}
