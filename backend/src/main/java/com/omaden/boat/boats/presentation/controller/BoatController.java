package com.omaden.boat.boats.presentation.controller;

import com.omaden.boat.boats.application.dto.BoatDto;
import com.omaden.boat.boats.application.mapper.BoatMapper;
import com.omaden.boat.boats.application.usecase.CreateBoatUseCase;
import com.omaden.boat.boats.application.usecase.DeleteBoatUseCase;
import com.omaden.boat.boats.application.usecase.GetAllBoatsUseCase;
import com.omaden.boat.boats.application.usecase.GetBoatUseCase;
import com.omaden.boat.boats.application.usecase.UpdateBoatUseCase;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boats")
public class BoatController {

    private final GetAllBoatsUseCase getAllBoatsUseCase;
    private final GetBoatUseCase getBoatUseCase;
    private final CreateBoatUseCase createBoatUseCase;
    private final UpdateBoatUseCase updateBoatUseCase;
    private final DeleteBoatUseCase deleteBoatUseCase;
    private final BoatMapper boatMapper;

    public BoatController(
            GetAllBoatsUseCase getAllBoatsUseCase,
            GetBoatUseCase getBoatUseCase,
            CreateBoatUseCase createBoatUseCase,
            UpdateBoatUseCase updateBoatUseCase,
            DeleteBoatUseCase deleteBoatUseCase,
            BoatMapper boatMapper) {
        this.getAllBoatsUseCase = getAllBoatsUseCase;
        this.getBoatUseCase = getBoatUseCase;
        this.createBoatUseCase = createBoatUseCase;
        this.updateBoatUseCase = updateBoatUseCase;
        this.deleteBoatUseCase = deleteBoatUseCase;
        this.boatMapper = boatMapper;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<BoatDto>> getAllBoats() {
        List<BoatDto> boats =
                getAllBoatsUseCase.execute().stream()
                        .map(boatMapper::toDto)
                        .collect(Collectors.toList());
        return ResponseEntity.ok(boats);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<BoatDto> getBoatById(@PathVariable Long id) {
        BoatDto boat = boatMapper.toDto(getBoatUseCase.execute(id));
        return ResponseEntity.ok(boat);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<BoatDto> createBoat(@Valid @RequestBody BoatDto boatDto) {
        BoatDto createdBoat = boatMapper.toDto(createBoatUseCase.execute(boatDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBoat);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<BoatDto> updateBoat(
            @PathVariable Long id, @Valid @RequestBody BoatDto boatDto) {
        BoatDto updatedBoat = boatMapper.toDto(updateBoatUseCase.execute(id, boatDto));
        return ResponseEntity.ok(updatedBoat);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBoat(@PathVariable Long id) {
        deleteBoatUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
