package softuni.exam.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "cars",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"make", "model", "kilometers"})})
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String make;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private Integer kilometers;

    @Column(nullable = false, name = "registered_on")
    private LocalDate registeredOn;

    @OneToMany(targetEntity = Picture.class, mappedBy = "car", fetch = FetchType.EAGER)
    private Set<Picture> pictures;

    public Car() {
    }

    public Car(Long id, String make, String model, Integer kilometers, LocalDate registeredOn) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.kilometers = kilometers;
        this.registeredOn = registeredOn;
        this.pictures = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(Set<Picture> pictures) {
        this.pictures = pictures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(id, car.id) &&
                Objects.equals(make, car.make) &&
                Objects.equals(model, car.model) &&
                Objects.equals(kilometers, car.kilometers) &&
                Objects.equals(registeredOn, car.registeredOn) &&
                Objects.equals(pictures, car.pictures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, make, model, kilometers, registeredOn, pictures);
    }
}
