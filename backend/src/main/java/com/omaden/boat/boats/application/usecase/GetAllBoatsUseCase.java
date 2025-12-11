package com.omaden.boat.boats.application.usecase;

import com.omaden.boat.boats.domain.model.Boat;
import com.omaden.boat.boats.domain.repository.BoatRepository;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class GetAllBoatsUseCase {

    private final BoatRepository boatRepository;

    public GetAllBoatsUseCase(BoatRepository boatRepository) {
        this.boatRepository = boatRepository;
    }

    public List<Boat> execute() {
        return boatRepository.findAll();
    }
}
