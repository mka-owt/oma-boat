package com.omaden.boat.boats.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaBoatRepository extends JpaRepository<BoatJpaEntity, Long> {}
