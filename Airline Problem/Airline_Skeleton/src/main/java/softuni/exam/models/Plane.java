package softuni.exam.models;

import javax.persistence.*;

@Entity
@Table(name = "planes")
public class Plane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "register_number", nullable = false, unique = true)
    private String registerNumber;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private String airline;

    public Plane() {
    }

    public Plane( String registerNumber, Integer capacity, String airline) {
        this.registerNumber = registerNumber;
        this.capacity = capacity;
        this.airline = airline;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }
}
