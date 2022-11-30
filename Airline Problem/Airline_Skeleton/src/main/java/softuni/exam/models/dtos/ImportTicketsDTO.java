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
public class ImportTicketsDTO {

    @Size(min = 2)
    @XmlElement(name = "serial-number")
    private String serialNumber;

    @Positive
    @XmlElement
    private BigDecimal price;

    @XmlElement(name = "take-off")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime takeoff;

    @XmlElement(name = "from-town")
    private ImportTicketsFromTownDTO fromTown;

    @XmlElement(name = "to-town")
    private ImportTicketsToTownDTO toTown;

    @XmlElement
    private ImportTicketsPassengerDTO passenger;

    @XmlElement
    private ImportTicketsPlaneDTO plane;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getTakeoff() {
        return takeoff;
    }

    public void setTakeoff(LocalDateTime takeoff) {
        this.takeoff = takeoff;
    }

    public ImportTicketsFromTownDTO getFromTown() {
        return fromTown;
    }

    public void setFromTown(ImportTicketsFromTownDTO fromTown) {
        this.fromTown = fromTown;
    }

    public ImportTicketsToTownDTO getToTown() {
        return toTown;
    }

    public void setToTown(ImportTicketsToTownDTO toTown) {
        this.toTown = toTown;
    }

    public ImportTicketsPassengerDTO getPassenger() {
        return passenger;
    }

    public void setPassenger(ImportTicketsPassengerDTO passenger) {
        this.passenger = passenger;
    }

    public ImportTicketsPlaneDTO getPlane() {
        return plane;
    }

    public void setPlane(ImportTicketsPlaneDTO plane) {
        this.plane = plane;
    }
}
