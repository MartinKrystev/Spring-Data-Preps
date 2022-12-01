package softuni.exam.models.dtos;

import softuni.exam.util.LocalDateTimeAdapter;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@XmlAccessorType(XmlAccessType.FIELD)
public class ImportOffersDTO {

    @Size(min = 5)
    @XmlElement(name = "description")
    private String description;

    @Positive
    @XmlElement(name = "price")
    private BigDecimal price;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    @XmlElement(name = "added-on")
    private LocalDateTime addedOn;

    @XmlElement(name = "has-gold-status")
    private Boolean hasGoldStatus;

    @XmlElement(name = "car")
    private ImportOffersCarDTO car;

    @XmlElement(name = "seller")
    private ImportOffersSellerDTO seller;

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

    public LocalDateTime getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDateTime addedOn) {
        this.addedOn = addedOn;
    }

    public Boolean getHasGoldStatus() {
        return hasGoldStatus;
    }

    public void setHasGoldStatus(Boolean hasGoldStatus) {
        this.hasGoldStatus = hasGoldStatus;
    }

    public ImportOffersCarDTO getCar() {
        return car;
    }

    public void setCar(ImportOffersCarDTO car) {
        this.car = car;
    }

    public ImportOffersSellerDTO getSeller() {
        return seller;
    }

    public void setSeller(ImportOffersSellerDTO seller) {
        this.seller = seller;
    }
}
