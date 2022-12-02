package softuni.exam.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dtos.ImportTeamsDTO;
import softuni.exam.domain.dtos.ImportTeamsRootDTO;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.TeamRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final PictureService pictureService;
    private final Validator validator;
    private final ModelMapper mapper;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository,
                           PictureService pictureService) {
        this.teamRepository = teamRepository;
        this.pictureService = pictureService;

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ModelMapper();
    }

    @Override
    public String importTeams() throws FileNotFoundException, JAXBException {
        final FileReader fileReader = new FileReader(Path.of("src", "main", "resources", "files", "xml", "teams.xml").toFile());

        JAXBContext context = JAXBContext.newInstance(ImportTeamsRootDTO.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ImportTeamsRootDTO importPlanesDTOs = (ImportTeamsRootDTO) unmarshaller.unmarshal(fileReader);

        List<String> result = new ArrayList<>();
        for (ImportTeamsDTO t : importPlanesDTOs.getTeams()) {

            Set<ConstraintViolation<ImportTeamsDTO>> validationErrors = validator.validate(t);
            if (validationErrors.isEmpty()) {

                Team optTeam = findByName(t.getName());
                Picture optPicture = this.pictureService.findByUrl(t.getPicture().getUrl());
                if (optTeam == null && optPicture != null) {

                    Team teamToSave = this.mapper.map(t, Team.class);
                    this.teamRepository.save(teamToSave);
                    result.add(String.format("Successfully imported - %s",
                            teamToSave.getName()));
                } else {
                    result.add("Invalid team");
                }
            } else {
                result.add("Invalid team");
            }
        }
        return String.join(System.lineSeparator(), result);
    }

    @Override
    public boolean areImported() {
        return this.teamRepository.count() > 0;
    }

    @Override
    public String readTeamsXmlFile() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/xml/teams.xml"));
    }

    @Override
    public Team findByName(String name) {
        return this.teamRepository.findByName(name).orElse(null);
    }
}
