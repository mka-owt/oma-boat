package com.omaden.boat.boats.application.usecase;

import com.omaden.boat.boats.application.dto.BoatDto;
import com.omaden.boat.boats.domain.exception.BoatNotFoundException;
import com.omaden.boat.boats.domain.model.Boat;
import com.omaden.boat.boats.domain.repository.BoatRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UpdateBoatUseCase {

    private final BoatRepository boatRepository;

    public UpdateBoatUseCase(BoatRepository boatRepository) {
        this.boatRepository = boatRepository;
    }

    @Transactional
    public Boat execute(Long id, BoatDto boatDto) {
        Boat boat = boatRepository.findById(id).orElseThrow(() -> new BoatNotFoundException(id));

        boat.update(
                boatDto.getName(),
                boatDto.getDescription(),
                boatDto.getLength(),
                boatDto.getYear());

        return boatRepository.save(boat);
    }
}
