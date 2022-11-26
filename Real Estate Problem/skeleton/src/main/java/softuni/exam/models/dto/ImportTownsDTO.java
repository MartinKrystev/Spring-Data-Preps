package softuni.exam.models.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class ImportTownsDTO {

    @Size(min = 2)
    private String townName;

    @Min(2)
    @Positive
    private Integer population;

    public ImportTownsDTO() {
    }

    public ImportTownsDTO(String townName, Integer population) {
        this.townName = townName;
        this.population = population;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }
}
