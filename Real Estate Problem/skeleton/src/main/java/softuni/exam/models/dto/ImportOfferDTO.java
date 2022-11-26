package softuni.exam.models.dto;

import softuni.exam.util.LocalDateAdapter;

import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalDate;

@XmlRootElement(name = "offer")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportOfferDTO {

    @Positive
    @XmlElement(name = "price")
    private BigDecimal price;

    @XmlElement(name = "agent")
    private AgentNameDTO name;

    @XmlElement(name = "apartment")
    private ApartmentIdDTO apartment;

    @XmlElement(name = "publishedOn")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate publishedOn;

    public ImportOfferDTO() {
    }


    public ImportOfferDTO setName(AgentNameDTO name) {
        this.name = name;
        return this;
    }

    public void setPublishedOn(LocalDate publishedOn) {
        this.publishedOn = publishedOn;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public AgentNameDTO getAgent() {
        return name;
    }

    public void setAgent(AgentNameDTO name) {
        this.name = name;
    }

    public ApartmentIdDTO getApartment() {
        return apartment;
    }

    public void setApartment(ApartmentIdDTO apartment) {
        this.apartment = apartment;
    }

    public LocalDate getPublishedOn() {
        return publishedOn;
    }

}
