package com.omaden.boat.boats.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.omaden.boat.boats.application.dto.BoatDto;
import com.omaden.boat.boats.domain.model.Boat;
import com.omaden.boat.boats.domain.repository.BoatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateBoatUseCase Tests")
class CreateBoatUseCaseTest {

    @Mock private BoatRepository boatRepository;

    private CreateBoatUseCase createBoatUseCase;
    private BoatDto testBoatDto;

    @BeforeEach
    void setUp() {
        createBoatUseCase = new CreateBoatUseCase(boatRepository);
        testBoatDto = new BoatDto(null, "Titanic", "Titanic description");
    }

    @Test
    @DisplayName("Should create a new boat")
    void shouldCreateBoat() {
        // Given
        Boat savedBoat = Boat.reconstitute(1L, "Titanic", "Titanic description");
        when(boatRepository.save(any(Boat.class))).thenReturn(savedBoat);

        // When
        Boat result = createBoatUseCase.execute(testBoatDto);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Titanic", result.getName());
        verify(boatRepository).save(any(Boat.class));
    }

    @Test
    @DisplayName("Should create boat with length and year")
    void shouldCreateBoatWithLengthAndYear() {
        // Given
        BoatDto boatDto = new BoatDto(null, "Titanic", "Description", 12.5, 2020);
        Boat savedBoat = Boat.reconstitute(1L, "Titanic", "Description", 12.5, 2020);
        when(boatRepository.save(any(Boat.class))).thenReturn(savedBoat);

        // When
        Boat result = createBoatUseCase.execute(boatDto);

        // Then
        assertNotNull(result);
        assertEquals(12.5, result.getLength());
        assertEquals(2020, result.getYear());
        verify(boatRepository).save(any(Boat.class));
    }
}
