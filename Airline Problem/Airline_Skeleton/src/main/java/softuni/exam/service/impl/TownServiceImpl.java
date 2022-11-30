package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.Town;
import softuni.exam.models.dtos.ImportTownsDTO;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class TownServiceImpl implements TownService {
    private final TownRepository townRepository;
    private final Gson gson;
    private final Validator validator;
    private final ModelMapper mapper;

    @Autowired
    public TownServiceImpl(TownRepository townRepository) {
        this.townRepository = townRepository;

        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "json", "towns.json");
        return Files.readString(path);
    }

    @Override
    public String importTowns() throws IOException {
        String json = this.readTownsFileContent();

        ImportTownsDTO[] importTownsDTOs = this.gson.fromJson(json, ImportTownsDTO[].class);

        List<String> result = new ArrayList<>();
        for (ImportTownsDTO t : importTownsDTOs) {

            Set<ConstraintViolation<ImportTownsDTO>> validationErrors = validator.validate(t);
            if (validationErrors.isEmpty()) {

                Town optTown = findByName(t.getName());
                if (optTown == null) {

                    Town townToSave = this.mapper.map(t, Town.class);
                    this.townRepository.save(townToSave);

                    result.add(String.format("Successfully imported Town %s - %d",
                            townToSave.getName(), townToSave.getPopulation()));
                } else {
                    result.add("Invalid Town");
                }
            } else {
                result.add("Invalid Town");
            }
        }
        return String.join(System.lineSeparator(), result);
    }

    @Override
    public Town findByName(String name) {
        return this.townRepository.findByName(name).orElse(null);
    }
}
