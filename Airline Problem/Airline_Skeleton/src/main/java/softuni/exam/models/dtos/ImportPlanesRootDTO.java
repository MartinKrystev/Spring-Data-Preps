package softuni.exam.models.dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "planes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportPlanesRootDTO {

    @XmlElement(name = "plane")
    private List<ImportBasePlaneDTO> planes;

    public List<ImportBasePlaneDTO> getPlanes() {
        return planes;
    }

    public void setPlanes(List<ImportBasePlaneDTO> planes) {
        this.planes = planes;
    }
}
