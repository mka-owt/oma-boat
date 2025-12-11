package com.omaden.boat.boats.application.mapper;

import com.omaden.boat.boats.application.dto.BoatDto;
import com.omaden.boat.boats.domain.model.Boat;
import org.springframework.stereotype.Component;

@Component
public class BoatMapper {

    public BoatDto toDto(Boat boat) {
        if (boat == null) {
            return null;
        }
        return new BoatDto(
                boat.getId(),
                boat.getName(),
                boat.getDescription(),
                boat.getLength(),
                boat.getYear());
    }

    public Boat toDomain(BoatDto dto) {
        if (dto == null) {
            return null;
        }
        if (dto.getId() != null) {
            if (dto.getLength() != null || dto.getYear() != null) {
                return Boat.reconstitute(
                        dto.getId(),
                        dto.getName(),
                        dto.getDescription(),
                        dto.getLength(),
                        dto.getYear());
            }
            return Boat.reconstitute(dto.getId(), dto.getName(), dto.getDescription());
        }
        if (dto.getLength() != null || dto.getYear() != null) {
            return Boat.create(dto.getName(), dto.getDescription(), dto.getLength(), dto.getYear());
        }
        return Boat.create(dto.getName(), dto.getDescription());
    }
}
