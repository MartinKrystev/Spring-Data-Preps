package com.example.football.models.dto;

import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "stat")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportStatsDTO {

    @XmlElement(name = "passing")
    @Positive
    private Double passing;

    @XmlElement(name = "shooting")
    @Positive
    private Double shooting;

    @XmlElement(name = "endurance")
    @Positive
    private Double endurance;

    public ImportStatsDTO() {
    }

    public ImportStatsDTO(Double passing, Double shooting, Double endurance) {
        this.passing = passing;
        this.shooting = shooting;
        this.endurance = endurance;
    }

    public Double getPassing() {
        return passing;
    }

    public void setPassing(Double passing) {
        this.passing = passing;
    }

    public Double getShooting() {
        return shooting;
    }

    public void setShooting(Double shooting) {
        this.shooting = shooting;
    }

    public Double getEndurance() {
        return endurance;
    }

    public void setEndurance(Double endurance) {
        this.endurance = endurance;
    }
}
