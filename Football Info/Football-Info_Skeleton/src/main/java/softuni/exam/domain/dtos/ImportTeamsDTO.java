package softuni.exam.domain.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ImportTeamsDTO {

    @XmlElement(name = "name")
    @Size(min = 3, max = 20)
    private String name;

    @XmlElement(name = "picture")
    @NotNull
    private ImportTeamsPictureDTO picture;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImportTeamsPictureDTO getPicture() {
        return picture;
    }

    public void setPicture(ImportTeamsPictureDTO picture) {
        this.picture = picture;
    }
}
