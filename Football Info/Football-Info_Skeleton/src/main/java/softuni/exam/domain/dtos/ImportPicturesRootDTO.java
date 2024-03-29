package softuni.exam.domain.dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "pictures")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportPicturesRootDTO {

    @XmlElement(name = "picture")
    private List<ImportPicturesDTO> pictures;

    public List<ImportPicturesDTO> getPictures() {
        return pictures;
    }

    public void setPictures(List<ImportPicturesDTO> pictures) {
        this.pictures = pictures;
    }
}
