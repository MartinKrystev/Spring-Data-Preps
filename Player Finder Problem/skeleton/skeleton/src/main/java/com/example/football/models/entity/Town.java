package com.example.football.models.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "towns")
public class Town {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = false)
    private String name;

    @Column(nullable = false)
    private Long population;

    @Column(name = "travel_guide", nullable = false, columnDefinition = "TEXT")
    private String travelGuide;

    @OneToMany(targetEntity = Team.class, mappedBy = "town")
    private Set<Team> teams;

    @OneToMany(targetEntity = Player.class, mappedBy = "town")
    private Set<Player> players;

    public Town() {
    }

    public Town(Long id, String name, Long population, String travelGuide, Set<Team> teams, Set<Player> players) {
        this.id = id;
        this.name = name;
        this.population = population;
        this.travelGuide = travelGuide;
        this.teams = teams;
        this.players = players;
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

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }

    public String getTravelGuide() {
        return travelGuide;
    }

    public void setTravelGuide(String travelGuide) {
        this.travelGuide = travelGuide;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Town town = (Town) o;
        return Objects.equals(id, town.id) &&
                Objects.equals(name, town.name) &&
                Objects.equals(population, town.population) &&
                Objects.equals(travelGuide, town.travelGuide);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, population, travelGuide);
    }
}
