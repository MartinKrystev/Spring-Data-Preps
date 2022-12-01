package softuni.exam.models.dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "offers")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportOffersRootDTO {

    @XmlElement(name = "offer")
    private List<ImportOffersDTO> offers;

    public List<ImportOffersDTO> getOffers() {
        return offers;
    }

    public void setOffers(List<ImportOffersDTO> offers) {
        this.offers = offers;
    }
}
