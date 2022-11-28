package exam.model.dtos;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class ImportCustomerDTO {

    @Size(min = 2)
    private String firstName;

    @Size(min = 2)
    private String lastName;

    @Email
    private String email;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private String registeredOn;

    private ImportCustomerTownDTO town;

    public ImportCustomerDTO() {
    }

    public ImportCustomerDTO(String firstName, String lastName, String email, String registeredOn, ImportCustomerTownDTO town) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.registeredOn = registeredOn;
        this.town = town;
    }

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
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(String registeredOn) {
        this.registeredOn = registeredOn;
    }

    public ImportCustomerTownDTO getTown() {
        return town;
    }

    public void setTown(ImportCustomerTownDTO town) {
        this.town = town;
    }
}
