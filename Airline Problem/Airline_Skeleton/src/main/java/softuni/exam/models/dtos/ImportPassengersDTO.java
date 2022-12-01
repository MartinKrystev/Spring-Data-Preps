package softuni.exam.models.dtos;

import softuni.exam.models.Town;

import javax.validation.constraints.Email;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class ImportPassengersDTO {

    @Size(min = 2)
    private String firstName;

    @Size(min = 2)
    private String lastName;

    @Positive
    private Integer age;

    private String phoneNumber;

    @Email
    private String email;

    private String town;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}