package com.example.football.models.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "stats")
public class Stat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double shooting;

    @Column(nullable = false)
    private Double passing;

    @Column(nullable = false)
    private Double endurance;

    @OneToMany(targetEntity = Player.class, mappedBy = "team")
    private Set<Player> players;

    public Stat() {
    }

    public Stat(Long id, Double shooting, Double passing, Double endurance, Set<Player> players) {
        this.id = id;
        this.shooting = shooting;
        this.passing = passing;
        this.endurance = endurance;
        this.players = players;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getShooting() {
        return shooting;
    }

    public void setShooting(Double shooting) {
        this.shooting = shooting;
    }

    public Double getPassing() {
        return passing;
    }

    public void setPassing(Double passing) {
        this.passing = passing;
    }

    public Double getEndurance() {
        return endurance;
    }

    public void setEndurance(Double endurance) {
        this.endurance = endurance;
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
        Stat stat = (Stat) o;
        return Objects.equals(id, stat.id) &&
                Objects.equals(shooting, stat.shooting) &&
                Objects.equals(passing, stat.passing) &&
                Objects.equals(endurance, stat.endurance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shooting, passing, endurance);
    }
}
