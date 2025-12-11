package com.omaden.boat.boats.infrastructure.persistence;

import jakarta.persistence.*;

@Entity
@Table(name = "boats")
public class BoatJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    @Column private Double length;

    @Column(name = "construction_year")
    private Integer year;

    protected BoatJpaEntity() {
        // Required by JPA
    }

    public BoatJpaEntity(Long id, String name, String description, Double length, Integer year) {
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
