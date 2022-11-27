package com.example.football.models.dto;

import com.example.football.models.entity.enums.PlayerPosition;
import com.example.football.util.LocalDateAdapter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

@XmlRootElement(name = "player")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportPlayerDTO {

    @Size(min = 3)
    @XmlElement(name = "first-name")
    private String firstName;

    @Size(min = 3)
    @XmlElement(name = "last-name")
    private String lastName;

    @Email
    @XmlElement(name = "email")
    private String email;

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @XmlElement(name = "birth-date")
    private LocalDate birthDate;

    @Enumerated
    @XmlElement(name = "position")
    private PlayerPosition position;

    @XmlElement(name = "town")
    private TownNameDTO town;

    @XmlElement(name = "team")
    private TeamNameDTO team;

    @XmlElement(name = "stat")
    private StatIdDTO stat;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public PlayerPosition getPosition() {
        return position;
    }

    public TownNameDTO getTown() {
        return town;
    }

    public TeamNameDTO getTeam() {
        return team;
    }

    public StatIdDTO getStat() {
        return stat;
    }
}
