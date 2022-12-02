package softuni.exam.domain.dtos;

public class ImportPlayersTeamDTO {

    private String name;

    private ImportPlayersTeamPictureDTO picture;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImportPlayersTeamPictureDTO getPicture() {
        return picture;
    }

    public void setPicture(ImportPlayersTeamPictureDTO picture) {
        this.picture = picture;
    }
}
