package softuni.exam.domain.dtos;

import softuni.exam.domain.enums.Position;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;
import java.math.BigDecimal;

public class ImportPlayersDTO {

    @NotNull
    private String firstName;

    @Size(min = 3, max = 15)
    private String lastName;

    @Min(1)
    @Max(99)
    private Integer number;

    @PositiveOrZero
    private BigDecimal salary;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Position position;

    @NotNull
    private ImportPlayersPictureDTO picture;

    @NotNull
    private ImportPlayersTeamDTO team;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public ImportPlayersPictureDTO getPicture() {
        return picture;
    }

    public void setPicture(ImportPlayersPictureDTO picture) {
        this.picture = picture;
    }

    public ImportPlayersTeamDTO getTeam() {
        return team;
    }

    public void setTeam(ImportPlayersTeamDTO team) {
        this.team = team;
    }
}
