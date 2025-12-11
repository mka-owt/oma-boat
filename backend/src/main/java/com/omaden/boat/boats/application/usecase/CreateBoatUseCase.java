package com.omaden.boat.boats.application.usecase;

import com.omaden.boat.boats.application.dto.BoatDto;
import com.omaden.boat.boats.domain.model.Boat;
import com.omaden.boat.boats.domain.repository.BoatRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateBoatUseCase {

    private final BoatRepository boatRepository;

    public CreateBoatUseCase(BoatRepository boatRepository) {
        this.boatRepository = boatRepository;
    }

    @Transactional
    public Boat execute(BoatDto boatDto) {
        Boat boat =
                Boat.create(
                        boatDto.getName(),
                        boatDto.getDescription(),
                        boatDto.getLength(),
                        boatDto.getYear());

        return boatRepository.save(boat);
    }
}
