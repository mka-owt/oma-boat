package com.omaden.boat.boats.domain.model;

import java.util.Objects;

public class Boat {
    private Long id;
    private String name;
    private String description;
    private Double length;
    private Integer year;

    private Boat(String name, String description, Double length, Integer year) {
        this.setName(name);
        this.setDescription(description);
        this.length = length;
        this.year = year;
    }

    private Boat(Long id, String name, String description, Double length, Integer year) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.length = length;
        this.year = year;
    }

    public static Boat create(String name, String description) {
        return new Boat(name, description, null, null);
    }

    public static Boat create(String name, String description, Double length, Integer year) {
        return new Boat(name, description, length, year);
    }

    public static Boat reconstitute(Long id, String name, String description) {
        return new Boat(id, name, description, null, null);
    }

    public static Boat reconstitute(
            Long id, String name, String description, Double length, Integer year) {
        return new Boat(id, name, description, length, year);
    }

    public void update(String name, String description) {
        this.setName(name);
        this.setDescription(description);
        this.length = null;
        this.year = null;
    }

    public void update(String name, String description, Double length, Integer year) {
        this.setName(name);
        this.setDescription(description);
        this.length = length;
        this.year = year;
    }

    private void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Boat name cannot be null or empty");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Boat name cannot exceed 100 characters");
        }
        this.name = name.trim();
    }

    private void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Boat description cannot be null or empty");
        }
        if (description.length() > 500) {
            throw new IllegalArgumentException("Boat description cannot exceed 500 characters");
        }
        this.description = description.trim();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getLength() {
        return length;
    }

    public Integer getYear() {
        return year;
    }

    public void assignId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("Boat already has an ID");
        }
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Boat boat = (Boat) o;
        return Objects.equals(id, boat.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
