package softuni.exam.models.dtos;

public class ExportPassengersWithTicketsCountDTO {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private int ticketCount;

    public ExportPassengersWithTicketsCountDTO() {
    }

    public ExportPassengersWithTicketsCountDTO(String firstName, String lastName, String email, String phoneNumber, int ticketCount) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.ticketCount = ticketCount;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("Passenger %s  %s", this.firstName, this.lastName)).append(System.lineSeparator());
        sb.append(String.format("\tEmail - %s", this.email)).append(System.lineSeparator());
        sb.append(String.format("\tPhone - %s", this.phoneNumber)).append(System.lineSeparator());
        sb.append(String.format("\tNumber of tickets - %d", this.ticketCount)).append(System.lineSeparator());

        return sb.toString();
    }
}
