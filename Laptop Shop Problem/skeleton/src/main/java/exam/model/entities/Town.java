package exam.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "towns")
public class Town extends BaseEntity{

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Integer population;

    @Column(nullable = false, name = "travel_guide", columnDefinition = "TEXT")
    private String travelGuide;

    @OneToMany(targetEntity = Shop.class, mappedBy = "town")
    private Set<Shop> shops;

    @OneToMany(targetEntity = Customer.class, mappedBy = "town")
    private Set<Customer> customers;

    public Town() {
    }

    public Town(String name, Integer population, String travelGuide) {
        this.name = name;
        this.population = population;
        this.travelGuide = travelGuide;
        this.shops = new HashSet<>();
        this.customers = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public String getTravelGuide() {
        return travelGuide;
    }

    public void setTravelGuide(String travelGuide) {
        this.travelGuide = travelGuide;
    }

    public Set<Shop> getShops() {
        return shops;
    }

    public void setShops(Set<Shop> shops) {
        this.shops = shops;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Town town = (Town) o;
        return Objects.equals(name, town.name) &&
                Objects.equals(population, town.population) &&
                Objects.equals(travelGuide, town.travelGuide)
                && Objects.equals(shops, town.shops) &&
                Objects.equals(customers, town.customers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, population, travelGuide, shops, customers);
    }
}
