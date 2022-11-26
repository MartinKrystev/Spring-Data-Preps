package softuni.exam.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "towns")
public class Town extends BaseEntity{

    @Column(name = "town_name", nullable = false, unique = true)
    private String townName;

    @Column(nullable = false)
    private Integer population;

    @OneToMany(targetEntity = Agent.class, mappedBy = "town")
    private Set<Agent> agents;

    @OneToMany(targetEntity = Apartment.class, mappedBy = "town")
    private Set<Apartment> apartments;

    public Town() {
    }

    public Town(String townName, Integer population, Set<Agent> agents, Set<Apartment> apartments) {
        this.townName = townName;
        this.population = population;
        this.agents = agents;
        this.apartments = apartments;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Set<Agent> getAgents() {
        return agents;
    }

    public void setAgents(Set<Agent> agents) {
        this.agents = agents;
    }

    public Set<Apartment> getApartments() {
        return apartments;
    }

    public void setApartments(Set<Apartment> apartments) {
        this.apartments = apartments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Town town = (Town) o;
        return Objects.equals(townName, town.townName) &&
                Objects.equals(population, town.population) &&
                Objects.equals(agents, town.agents) &&
                Objects.equals(apartments, town.apartments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(townName, population, agents, apartments);
    }
}
