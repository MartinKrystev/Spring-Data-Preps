package exam.model.dtos;

public class ImportCustomerTownDTO {

    private String name;

    public ImportCustomerTownDTO() {
    }

    public ImportCustomerTownDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
