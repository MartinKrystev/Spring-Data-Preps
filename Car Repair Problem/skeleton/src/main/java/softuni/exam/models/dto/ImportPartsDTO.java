package softuni.exam.models.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class ImportPartsDTO {

    @Size(min = 2, max = 19)
    private String partName;

    @Min(10)
    @Max(2_000)
    private Double price;

    @Positive
    private Integer quantity;

    public ImportPartsDTO() {
    }

    public ImportPartsDTO(String partName, Double price, Integer quantity) {
        this.partName = partName;
        this.price = price;
        this.quantity = quantity;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
