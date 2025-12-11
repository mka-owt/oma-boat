package com.omaden.boat.boats.infrastructure.persistence;

import com.omaden.boat.boats.domain.model.Boat;
import org.springframework.stereotype.Component;

@Component
public class BoatJpaMapper {

    public Boat toDomain(BoatJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return Boat.reconstitute(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getLength(),
                entity.getYear());
    }

    public BoatJpaEntity toJpa(Boat boat) {
        if (boat == null) {
            return null;
        }
        return new BoatJpaEntity(
                boat.getId(),
                boat.getName(),
                boat.getDescription(),
                boat.getLength(),
                boat.getYear());
    }
}
