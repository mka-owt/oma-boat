package com.omaden.boat.boats.infrastructure.adapter;

import com.omaden.boat.boats.domain.model.Boat;
import com.omaden.boat.boats.domain.repository.BoatRepository;
import com.omaden.boat.boats.infrastructure.persistence.BoatJpaEntity;
import com.omaden.boat.boats.infrastructure.persistence.BoatJpaMapper;
import com.omaden.boat.boats.infrastructure.persistence.JpaBoatRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class BoatRepositoryAdapter implements BoatRepository {

    private final JpaBoatRepository jpaRepository;
    private final BoatJpaMapper mapper;

    public BoatRepositoryAdapter(JpaBoatRepository jpaRepository, BoatJpaMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Boat save(Boat boat) {
        BoatJpaEntity entity = mapper.toJpa(boat);
        BoatJpaEntity savedEntity = jpaRepository.save(entity);
        Boat savedBoat = mapper.toDomain(savedEntity);

        if (boat.getId() == null) {
            boat.assignId(savedEntity.getId());
            return boat;
        }

        return savedBoat;
    }

    @Override
    public Optional<Boat> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Boat> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
}
