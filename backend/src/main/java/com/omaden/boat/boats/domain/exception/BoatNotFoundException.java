package com.omaden.boat.boats.domain.exception;

public class BoatNotFoundException extends RuntimeException {
    public BoatNotFoundException(Long id) {
        super("Boat not found with id: " + id);
    }
}
