package com.omaden.boat.boats.domain.repository;

import com.omaden.boat.boats.domain.model.Boat;
import java.util.List;
import java.util.Optional;

public interface BoatRepository {

    Boat save(Boat boat);

    Optional<Boat> findById(Long id);

    List<Boat> findAll();

    void delete(Long id);

    boolean existsById(Long id);
}
