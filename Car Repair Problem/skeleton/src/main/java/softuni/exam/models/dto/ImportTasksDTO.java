package softuni.exam.models.dto;

import softuni.exam.util.LocalDateTimeAdapter;

import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@XmlAccessorType(XmlAccessType.FIELD)
public class ImportTasksDTO {

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    @XmlElement(name = "date")
    private LocalDateTime date;

    @Positive
    @XmlElement(name = "price")
    private BigDecimal price;

    @XmlElement(name = "car")
    private ImportTasksCarDTO car;

    @XmlElement(name = "mechanic")
    private ImportTasksMechanicDTO mechanic;

    @XmlElement(name = "part")
    private ImportTasksPartDTO part;

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ImportTasksCarDTO getCar() {
        return car;
    }

    public void setCar(ImportTasksCarDTO car) {
        this.car = car;
    }

    public ImportTasksMechanicDTO getMechanic() {
        return mechanic;
    }

    public void setMechanic(ImportTasksMechanicDTO mechanic) {
        this.mechanic = mechanic;
    }

    public ImportTasksPartDTO getPart() {
        return part;
    }

    public void setPart(ImportTasksPartDTO part) {
        this.part = part;
    }
}
