package com.example.football.models.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, name = "stadium_name")
    private String stadiumName;

    @Column(nullable = false, name = "fan_base")
    private Integer fanBase;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String history;

    @ManyToOne
    private Town town;

    @OneToMany(targetEntity = Player.class, mappedBy = "team")
    private Set<Player> players;

    public Team() {
    }

    public Team(Long id, String name, String stadiumName, Integer fanBase, String history, Town town, Set<Player> players) {
        this.id = id;
        this.name = name;
        this.stadiumName = stadiumName;
        this.fanBase = fanBase;
        this.history = history;
        this.town = town;
        this.players = new HashSet<>();
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

    public String getStadiumName() {
        return stadiumName;
    }

    public void setStadiumName(String stadiumName) {
        this.stadiumName = stadiumName;
    }

    public Integer getFanBase() {
        return fanBase;
    }

    public void setFanBase(Integer fanBase) {
        this.fanBase = fanBase;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
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
        Team team = (Team) o;
        return Objects.equals(id, team.id) &&
                Objects.equals(name, team.name) &&
                Objects.equals(stadiumName, team.stadiumName) &&
                Objects.equals(fanBase, team.fanBase) &&
                Objects.equals(history, team.history) &&
                Objects.equals(town, team.town);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, stadiumName, fanBase, history, town
        );
    }
}
