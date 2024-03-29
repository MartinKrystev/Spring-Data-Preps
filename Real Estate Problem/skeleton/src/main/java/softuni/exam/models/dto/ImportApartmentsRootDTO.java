package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "apartments")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportApartmentsRootDTO {

    @XmlElement(name = "apartment")
    private List<ImportApartmentsDTO> apartments;

    public List<ImportApartmentsDTO> getApartments() {
        return apartments;
    }

    public void setApartments(List<ImportApartmentsDTO> apartments) {
        this.apartments = apartments;
    }
}
