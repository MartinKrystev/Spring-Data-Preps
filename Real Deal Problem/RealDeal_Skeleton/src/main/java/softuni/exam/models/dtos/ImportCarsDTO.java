package softuni.exam.models.dtos;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class ImportCarsDTO {

    @Size(min = 2, max = 20)
    private String make;

    @Size(min = 2, max = 20)
    private String model;

    @Positive
    private Integer kilometers;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private String registeredOn;

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getKilometers() {
        return kilometers;
    }

    public void setKilometers(Integer kilometers) {
        this.kilometers = kilometers;
    }

    public String getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(String registeredOn) {
        this.registeredOn = registeredOn;
    }
}
