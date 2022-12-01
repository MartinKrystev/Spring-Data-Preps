package softuni.exam.models.dtos;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class ImportPicturesDTO {

    @Size(min = 2, max = 20)
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String dateAndTime;

    @Positive
    private Integer car;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public Integer getCar() {
        return car;
    }

    public void setCarId(Integer car) {
        this.car = car;
    }
}
