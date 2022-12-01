package softuni.exam.models.dtos;

import java.time.LocalDate;

public class ExportCarsWithPicsCountDTO {

    private String make;

    private String model;

    private Integer kilometers;

    private LocalDate registeredOn;

    private int countPictures;

    public ExportCarsWithPicsCountDTO() {
    }

    public ExportCarsWithPicsCountDTO(String make, String model, Integer kilometers, LocalDate registeredOn, int countPictures) {
        this.make = make;
        this.model = model;
        this.kilometers = kilometers;
        this.registeredOn = registeredOn;
        this.countPictures = countPictures;
    }

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

    public LocalDate getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(LocalDate registeredOn) {
        this.registeredOn = registeredOn;
    }

    public int getCountPictures() {
        return countPictures;
    }

    public void setCountPictures(int countPictures) {
        this.countPictures = countPictures;
    }
}
