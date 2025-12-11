package com.omaden.boat.boats.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.omaden.boat.boats.application.dto.BoatDto;
import com.omaden.boat.boats.domain.exception.BoatNotFoundException;
import com.omaden.boat.boats.domain.model.Boat;
import com.omaden.boat.boats.domain.repository.BoatRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateBoatUseCase Tests")
class UpdateBoatUseCaseTest {

    @Mock private BoatRepository boatRepository;

    private UpdateBoatUseCase updateBoatUseCase;
    private Boat testBoat;

    @BeforeEach
    void setUp() {
        updateBoatUseCase = new UpdateBoatUseCase(boatRepository);
        testBoat = Boat.reconstitute(1L, "Titanic", "Titanic description");
    }

    @Test
    @DisplayName("Should update existing boat")
    void shouldUpdateBoat() {
        // Given
        BoatDto updateDto = new BoatDto(1L, "Any Boat", "Updated description");
        Boat updatedBoat = Boat.reconstitute(1L, "Any Boat", "Updated description");

        when(boatRepository.findById(1L)).thenReturn(Optional.of(testBoat));
        when(boatRepository.save(any(Boat.class))).thenReturn(updatedBoat);

        // When
        Boat result = updateBoatUseCase.execute(1L, updateDto);

        // Then
        assertNotNull(result);
        assertEquals("Any Boat", result.getName());
        assertEquals("Updated description", result.getDescription());
        verify(boatRepository).findById(1L);
        verify(boatRepository).save(any(Boat.class));
    }

    @Test
    @DisplayName("Should throw exception when boat not found")
    void shouldThrowExceptionWhenBoatNotFound() {
        // Given
        BoatDto updateDto = new BoatDto(1L, "Any Boat", "Updated description");
        when(boatRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        BoatNotFoundException exception =
                assertThrows(
                        BoatNotFoundException.class,
                        () -> updateBoatUseCase.execute(999L, updateDto));
        assertEquals("Boat not found with id: 999", exception.getMessage());
        verify(boatRepository).findById(999L);
        verify(boatRepository, never()).save(any(Boat.class));
    }
}
