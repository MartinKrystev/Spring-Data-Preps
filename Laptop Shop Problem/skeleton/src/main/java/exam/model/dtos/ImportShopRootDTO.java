package exam.model.dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "shops")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportShopRootDTO {

    @XmlElement(name = "shop")
    List<ImportShopDTO> shops;

    public List<ImportShopDTO> getShops() {
        return shops;
    }

    public void setShops(List<ImportShopDTO> shops) {
        this.shops = shops;
    }
}
