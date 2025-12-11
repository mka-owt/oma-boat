package com.omaden.boat.boats.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
@DisplayName("GetBoatUseCase Tests")
class GetBoatUseCaseTest {

    @Mock private BoatRepository boatRepository;

    private GetBoatUseCase getBoatUseCase;
    private Boat testBoat;

    @BeforeEach
    void setUp() {
        getBoatUseCase = new GetBoatUseCase(boatRepository);
        testBoat = Boat.reconstitute(1L, "Titanic", "Titanic description");
    }

    @Test
    @DisplayName("Should get boat by id")
    void shouldGetBoatById() {
        // Given
        when(boatRepository.findById(1L)).thenReturn(Optional.of(testBoat));

        // When
        Boat result = getBoatUseCase.execute(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Titanic", result.getName());
        verify(boatRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when boat not found")
    void shouldThrowExceptionWhenBoatNotFound() {
        // Given
        when(boatRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        BoatNotFoundException exception =
                assertThrows(BoatNotFoundException.class, () -> getBoatUseCase.execute(999L));
        assertEquals("Boat not found with id: 999", exception.getMessage());
        verify(boatRepository).findById(999L);
    }
}
