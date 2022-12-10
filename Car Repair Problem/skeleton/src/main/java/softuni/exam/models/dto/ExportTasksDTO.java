package softuni.exam.models.dto;

import softuni.exam.models.entity.CarType;

import java.math.BigDecimal;

public class ExportTasksDTO {

    private String make;

    private String model;

    private String firstName;

    private String lastName;

    private Integer kilometers;

    private Long id;

    private Double engine;

    private BigDecimal price;



    public ExportTasksDTO() {
    }

    public ExportTasksDTO(String make, String model, String firstName, String lastName,
                          Integer kilometers, Long id, Double engine, BigDecimal price) {
        this.make = make;
        this.model = model;
        this.firstName = firstName;
        this.lastName = lastName;
        this.kilometers = kilometers;
        this.id = id;
        this.engine = engine;
        this.price = price;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getKilometers() {
        return kilometers;
    }

    public void setKilometers(Integer kilometers) {
        this.kilometers = kilometers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getEngine() {
        return engine;
    }

    public void setEngine(Double engine) {
        this.engine = engine;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
