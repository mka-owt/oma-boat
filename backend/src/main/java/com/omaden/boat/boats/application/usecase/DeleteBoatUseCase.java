package com.omaden.boat.boats.application.usecase;

import com.omaden.boat.boats.domain.exception.BoatNotFoundException;
import com.omaden.boat.boats.domain.repository.BoatRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DeleteBoatUseCase {

    private final BoatRepository boatRepository;

    public DeleteBoatUseCase(BoatRepository boatRepository) {
        this.boatRepository = boatRepository;
    }

    @Transactional
    public void execute(Long id) {
        if (!boatRepository.existsById(id)) {
            throw new BoatNotFoundException(id);
        }
        boatRepository.delete(id);
    }
}
