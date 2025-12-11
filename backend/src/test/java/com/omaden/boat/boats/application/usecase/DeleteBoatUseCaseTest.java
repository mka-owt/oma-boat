package com.omaden.boat.boats.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.omaden.boat.boats.domain.exception.BoatNotFoundException;
import com.omaden.boat.boats.domain.repository.BoatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteBoatUseCase Tests")
class DeleteBoatUseCaseTest {

    @Mock private BoatRepository boatRepository;

    private DeleteBoatUseCase deleteBoatUseCase;

    @BeforeEach
    void setUp() {
        deleteBoatUseCase = new DeleteBoatUseCase(boatRepository);
    }

    @Test
    @DisplayName("Should delete boat")
    void shouldDeleteBoat() {
        // Given
        when(boatRepository.existsById(1L)).thenReturn(true);
        doNothing().when(boatRepository).delete(1L);

        // When
        deleteBoatUseCase.execute(1L);

        // Then
        verify(boatRepository).existsById(1L);
        verify(boatRepository).delete(1L);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent boat")
    void shouldThrowExceptionWhenDeletingNonExistentBoat() {
        // Given
        when(boatRepository.existsById(999L)).thenReturn(false);

        // When & Then
        BoatNotFoundException exception =
                assertThrows(BoatNotFoundException.class, () -> deleteBoatUseCase.execute(999L));
        assertEquals("Boat not found with id: 999", exception.getMessage());
        verify(boatRepository).existsById(999L);
        verify(boatRepository, never()).delete(anyLong());
    }
}
