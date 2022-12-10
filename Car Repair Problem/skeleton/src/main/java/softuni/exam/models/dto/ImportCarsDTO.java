package softuni.exam.models.dto;

import softuni.exam.models.entity.CarType;

import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ImportCarsDTO {

    @Size(min = 2, max = 30)
    @XmlElement(name = "carMake")
    private String carMake;

    @Size(min = 2, max = 30)
    @XmlElement(name = "carModel")
    private String carModel;

    @Positive
    @XmlElement(name = "year")
    private Integer year;

    @Size(min = 2, max = 30)
    @XmlElement(name = "plateNumber")
    private String plateNumber;

    @Positive
    @XmlElement(name = "kilometers")
    private Integer kilometers;

    @Min(1)
    @XmlElement(name = "engine")
    private Double engine;

    @Enumerated
    @XmlElement(name = "carType")
    private CarType carType;

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Integer getKilometers() {
        return kilometers;
    }

    public void setKilometers(Integer kilometers) {
        this.kilometers = kilometers;
    }

    public Double getEngine() {
        return engine;
    }

    public void setEngine(Double engine) {
        this.engine = engine;
    }

    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }
}
