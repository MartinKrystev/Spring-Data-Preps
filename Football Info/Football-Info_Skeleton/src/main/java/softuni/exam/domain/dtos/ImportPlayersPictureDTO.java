package softuni.exam.domain.dtos;

import javax.validation.constraints.NotNull;

public class ImportPlayersPictureDTO {

    @NotNull
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
