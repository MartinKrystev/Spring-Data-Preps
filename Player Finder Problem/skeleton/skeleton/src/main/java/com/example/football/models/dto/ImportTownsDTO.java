package com.example.football.models.dto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class ImportTownsDTO {

    @Size(min = 2)
    private String name;

    @Positive
    private Long population;

    @Size(min = 10)
    private String travelGuide;

    public ImportTownsDTO() {
    }

    public ImportTownsDTO(String name, Long population, String travelGuide) {
        this.name = name;
        this.population = population;
        this.travelGuide = travelGuide;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }

    public String getTravelGuide() {
        return travelGuide;
    }

    public void setTravelGuide(String travelGuide) {
        this.travelGuide = travelGuide;
    }
}
