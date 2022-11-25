package softuni.exam.models.entity;

import javax.persistence.*;

@Entity
@Table(name = "cities")
public class City {
    //•	id – accepts integer values, a primary identification field, an auto incremented field.
    //•	city name – a char sequence (between 2 to 60 inclusive). The values are unique in the database. Cannot be null.
    //•	description – accepts very long char sequence (min 2 symbols).
    //•	population – accepts number values that are more than or equal to 500. Cannot be null.
    //•	Constraint: The cities table has а relation with the countries table.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "city_name", nullable = false, unique = true)
    private String cityName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer population;

    @ManyToOne
    private Country country;

    public City() {
    }

    public City(String cityName, String description, Integer population, Country country) {
        this.cityName = cityName;
        this.description = description;
        this.population = population;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
