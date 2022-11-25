package softuni.exam.models.entity;

import javax.persistence.*;

@Entity
@Table(name = "countries")
public class Country {
//•	id – accepts integer values, a primary identification field, an auto incremented field.
//•	country name – accepts char sequence (between 2 to 60 inclusive). The values are unique in the database. Cannot be null.
//•	currency – accepts char sequences (between 2 and 20 inclusive). Cannot be null.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String currency;

//    @OneToMany(targetEntity = City.class, mappedBy = "country")

    public Country() {
    }

    public Country(String name, String currency) {
        this.name = name;
        this.currency = currency;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
