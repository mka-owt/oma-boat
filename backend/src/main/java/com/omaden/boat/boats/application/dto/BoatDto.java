package com.omaden.boat.boats.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BoatDto {
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(min = 1, max = 500, message = "Description must be between 1 and 500 characters")
    private String description;

    private Double length;

    private Integer year;

    public BoatDto() {}

    public BoatDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.length = null;
        this.year = null;
    }

    public BoatDto(Long id, String name, String description, Double length, Integer year) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.length = length;
        this.year = year;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
