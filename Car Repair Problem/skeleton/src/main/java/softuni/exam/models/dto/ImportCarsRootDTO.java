package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "cars")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportCarsRootDTO {

    @XmlElement(name = "car")
    private List<ImportCarsDTO> cars;

    public List<ImportCarsDTO> getCars() {
        return cars;
    }

    public void setCars(List<ImportCarsDTO> cars) {
        this.cars = cars;
    }
}
