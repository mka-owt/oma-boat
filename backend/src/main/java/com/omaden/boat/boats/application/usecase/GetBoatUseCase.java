package com.omaden.boat.boats.application.usecase;

import com.omaden.boat.boats.domain.exception.BoatNotFoundException;
import com.omaden.boat.boats.domain.model.Boat;
import com.omaden.boat.boats.domain.repository.BoatRepository;
import org.springframework.stereotype.Component;

@Component
public class GetBoatUseCase {

    private final BoatRepository boatRepository;

    public GetBoatUseCase(BoatRepository boatRepository) {
        this.boatRepository = boatRepository;
    }

    public Boat execute(Long id) {
        return boatRepository.findById(id).orElseThrow(() -> new BoatNotFoundException(id));
    }
}
