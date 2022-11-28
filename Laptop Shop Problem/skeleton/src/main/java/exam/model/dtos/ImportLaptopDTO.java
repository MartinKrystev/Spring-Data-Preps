package exam.model.dtos;

import exam.model.entities.enums.WarrantyType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;
import java.math.BigDecimal;

public class ImportLaptopDTO {

    @Size(min = 9)
    private String macAddress;

    @Positive
    private Double cpuSpeed;

    @Min(8)
    @Max(128)
    private Integer ram;

    @Min(128)
    @Max(1024)
    private Integer storage;

    @Size(min = 10)
    private String description;

    @Positive
    private BigDecimal price;

    @Pattern(regexp = "^(BASIC|PREMIUM|LIFETIME)+$")
    private String warrantyType;

    private ImportLaptopShopDTO shop;

    public ImportLaptopDTO() {
    }

    public ImportLaptopDTO(String macAddress, Double cpuSpeed, Integer ram, Integer storage, String description,
                           BigDecimal price, String warrantyType, ImportLaptopShopDTO shop) {
        this.macAddress = macAddress;
        this.cpuSpeed = cpuSpeed;
        this.ram = ram;
        this.storage = storage;
        this.description = description;
        this.price = price;
        this.warrantyType = warrantyType;
        this.shop = shop;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Double getCpuSpeed() {
        return cpuSpeed;
    }

    public void setCpuSpeed(Double cpuSpeed) {
        this.cpuSpeed = cpuSpeed;
    }

    public Integer getRam() {
        return ram;
    }

    public void setRam(Integer ram) {
        this.ram = ram;
    }

    public Integer getStorage() {
        return storage;
    }

    public void setStorage(Integer storage) {
        this.storage = storage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getWarrantyType() {
        return warrantyType;
    }

    public void setWarrantyType(String warrantyType) {
        this.warrantyType = warrantyType;
    }

    public ImportLaptopShopDTO getShop() {
        return shop;
    }

    public void setShop(ImportLaptopShopDTO shop) {
        this.shop = shop;
    }
}
