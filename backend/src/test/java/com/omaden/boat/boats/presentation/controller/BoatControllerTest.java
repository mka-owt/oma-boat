package com.omaden.boat.boats.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omaden.boat.auth.infrastructure.security.JwtAuthenticationFilter;
import com.omaden.boat.auth.infrastructure.security.JwtTokenProvider;
import com.omaden.boat.boats.application.dto.BoatDto;
import com.omaden.boat.boats.application.mapper.BoatMapper;
import com.omaden.boat.boats.application.usecase.CreateBoatUseCase;
import com.omaden.boat.boats.application.usecase.DeleteBoatUseCase;
import com.omaden.boat.boats.application.usecase.GetAllBoatsUseCase;
import com.omaden.boat.boats.application.usecase.GetBoatUseCase;
import com.omaden.boat.boats.application.usecase.UpdateBoatUseCase;
import com.omaden.boat.boats.domain.exception.BoatNotFoundException;
import com.omaden.boat.boats.domain.model.Boat;
import com.omaden.boat.presentation.controller.GlobalExceptionHandler;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {BoatController.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {"jwt.secret=test-secret", "jwt.expiration=3600000"})
@DisplayName("BoatController Tests")
class BoatControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private GetAllBoatsUseCase getAllBoatsUseCase;
    @MockBean private GetBoatUseCase getBoatUseCase;
    @MockBean private CreateBoatUseCase createBoatUseCase;
    @MockBean private UpdateBoatUseCase updateBoatUseCase;
    @MockBean private DeleteBoatUseCase deleteBoatUseCase;
    @MockBean private BoatMapper boatMapper;
    @MockBean private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean private JwtTokenProvider jwtTokenProvider;

    @Autowired private ObjectMapper objectMapper;

    private Boat testBoat;
    private BoatDto testBoatDto;

    @BeforeEach
    void setUp() {
        testBoat = Boat.reconstitute(1L, "Titanic", "Titanic Description");
        testBoatDto = new BoatDto(1L, "Titanic", "Titanic Description");
    }

    @Test
    @DisplayName("Should get all boats")
    void shouldGetAllBoats() throws Exception {
        // Given
        Boat boat1 = Boat.reconstitute(1L, "Titanic", "Description 1");
        Boat boat2 = Boat.reconstitute(2L, "Any Boat", "Description 2");
        List<Boat> boats = Arrays.asList(boat1, boat2);

        BoatDto dto1 = new BoatDto(1L, "Titanic", "Description 1");
        BoatDto dto2 = new BoatDto(2L, "Any Boat", "Description 2");

        when(getAllBoatsUseCase.execute()).thenReturn(boats);
        when(boatMapper.toDto(boat1)).thenReturn(dto1);
        when(boatMapper.toDto(boat2)).thenReturn(dto2);

        // When & Then
        mockMvc.perform(get("/api/boats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Titanic"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Any Boat"));

        verify(getAllBoatsUseCase).execute();
    }

    @Test
    @DisplayName("Should get boat by id")
    void shouldGetBoatById() throws Exception {
        // Given
        when(getBoatUseCase.execute(1L)).thenReturn(testBoat);
        when(boatMapper.toDto(testBoat)).thenReturn(testBoatDto);

        // When & Then
        mockMvc.perform(get("/api/boats/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Titanic"))
                .andExpect(jsonPath("$.description").value("Titanic Description"));

        verify(getBoatUseCase).execute(1L);
        verify(boatMapper).toDto(testBoat);
    }

    @Test
    @DisplayName("Should return 404 when boat not found")
    void shouldReturn404WhenBoatNotFound() throws Exception {
        // Given
        when(getBoatUseCase.execute(999L)).thenThrow(new BoatNotFoundException(999L));

        // When & Then
        mockMvc.perform(get("/api/boats/999")).andExpect(status().isNotFound());

        verify(getBoatUseCase).execute(999L);
    }

    @Test
    @DisplayName("Should create a new boat")
    void shouldCreateBoat() throws Exception {
        // Given
        BoatDto createDto = new BoatDto(null, "Titanic", "Titanic Description");
        Boat createdBoat = Boat.reconstitute(1L, "Titanic", "Titanic Description");
        BoatDto createdDto = new BoatDto(1L, "Titanic", "Titanic Description");

        when(createBoatUseCase.execute(any(BoatDto.class))).thenReturn(createdBoat);
        when(boatMapper.toDto(createdBoat)).thenReturn(createdDto);

        // When & Then
        mockMvc.perform(
                        post("/api/boats")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Titanic"));

        verify(createBoatUseCase).execute(any(BoatDto.class));
        verify(boatMapper).toDto(createdBoat);
    }

    @Test
    @DisplayName("Should return 400 when creating boat with invalid data")
    void shouldReturn400WhenCreatingBoatWithInvalidData() throws Exception {
        // Given
        BoatDto invalidDto = new BoatDto(null, "", ""); // Empty name and description

        // When & Then
        mockMvc.perform(
                        post("/api/boats")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        verify(createBoatUseCase, never()).execute(any(BoatDto.class));
    }

    @Test
    @DisplayName("Should update boat")
    void shouldUpdateBoat() throws Exception {
        // Given
        BoatDto updateDto = new BoatDto(1L, "Any Boat", "Updated description");
        Boat updatedBoat = Boat.reconstitute(1L, "Any Boat", "Updated description");
        BoatDto updatedDto = new BoatDto(1L, "Any Boat", "Updated description");

        when(updateBoatUseCase.execute(eq(1L), any(BoatDto.class))).thenReturn(updatedBoat);
        when(boatMapper.toDto(updatedBoat)).thenReturn(updatedDto);

        // When & Then
        mockMvc.perform(
                        put("/api/boats/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Any Boat"));

        verify(updateBoatUseCase).execute(eq(1L), any(BoatDto.class));
        verify(boatMapper).toDto(updatedBoat);
    }

    @Test
    @DisplayName("Should delete boat")
    void shouldDeleteBoat() throws Exception {
        // Given
        doNothing().when(deleteBoatUseCase).execute(1L);

        // When & Then
        mockMvc.perform(delete("/api/boats/1")).andExpect(status().isNoContent());

        verify(deleteBoatUseCase).execute(1L);
    }
}
