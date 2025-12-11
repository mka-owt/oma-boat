package com.omaden.boat.boats.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.omaden.boat.boats.domain.model.Boat;
import com.omaden.boat.boats.domain.repository.BoatRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetAllBoatsUseCase Tests")
class GetAllBoatsUseCaseTest {

    @Mock private BoatRepository boatRepository;

    private GetAllBoatsUseCase getAllBoatsUseCase;

    @BeforeEach
    void setUp() {
        getAllBoatsUseCase = new GetAllBoatsUseCase(boatRepository);
    }

    @Test
    @DisplayName("Should get all boats")
    void shouldGetAllBoats() {
        // Given
        Boat boat1 = Boat.reconstitute(1L, "Titanic", "Description 1");
        Boat boat2 = Boat.reconstitute(2L, "Any Boat", "Description 2");
        List<Boat> boats = Arrays.asList(boat1, boat2);
        when(boatRepository.findAll()).thenReturn(boats);

        // When
        List<Boat> result = getAllBoatsUseCase.execute();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Titanic", result.get(0).getName());
        assertEquals("Any Boat", result.get(1).getName());
        verify(boatRepository).findAll();
    }
}
