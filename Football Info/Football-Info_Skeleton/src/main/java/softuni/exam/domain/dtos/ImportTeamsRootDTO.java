package softuni.exam.domain.dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "teams")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportTeamsRootDTO {

    @XmlElement(name = "team")
    private List<ImportTeamsDTO> teams;

    public List<ImportTeamsDTO> getTeams() {
        return teams;
    }

    public void setTeams(List<ImportTeamsDTO> teams) {
        this.teams = teams;
    }
}
