package com.example.football.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "stats")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportStatsRootDTO {

    @XmlElement(name = "stat")
    private List<ImportStatsDTO> stats;

    public ImportStatsRootDTO() {
    }

    public ImportStatsRootDTO(List<ImportStatsDTO> stats) {
        this.stats = stats;
    }

    public List<ImportStatsDTO> getStats() {
        return stats;
    }

    public void setStats(List<ImportStatsDTO> stats) {
        this.stats = stats;
    }
}
