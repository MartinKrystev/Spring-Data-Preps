package softuni.exam.models.dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "sellers")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportSellersRootDTO {

    @XmlElement(name = "seller")
    private List<ImportSellersDTO> sellers;

    public List<ImportSellersDTO> getSellers() {
        return sellers;
    }

    public void setSellers(List<ImportSellersDTO> sellers) {
        this.sellers = sellers;
    }
}
