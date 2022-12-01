package softuni.exam.models.dtos;

import softuni.exam.models.Rating;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ImportSellersDTO {

    @Size(min = 2, max = 20)
    @XmlElement(name = "first-name")
    private String firstName;

    @Size(min = 2, max = 20)
    @XmlElement(name = "last-name")
    private String lastName;

    @Email
    @XmlElement(name = "email")
    private String Email;

    @NotNull
    @Enumerated(EnumType.STRING)
    @XmlElement(name = "rating")
    private Rating rating;

    @NotNull
    @XmlElement(name = "town")
    private String town;

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

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
