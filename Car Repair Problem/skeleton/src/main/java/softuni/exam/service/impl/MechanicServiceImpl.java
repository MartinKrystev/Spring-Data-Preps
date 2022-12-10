package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportMechanicsDTO;
import softuni.exam.models.dto.ImportPartsDTO;
import softuni.exam.models.entity.Mechanic;
import softuni.exam.repository.MechanicRepository;
import softuni.exam.service.MechanicService;

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
public class MechanicServiceImpl implements MechanicService {
    private final MechanicRepository mechanicRepository;
    private final Gson gson;
    private final Validator validator;
    private final ModelMapper mapper;

    @Autowired
    public MechanicServiceImpl(MechanicRepository mechanicRepository) {
        this.mechanicRepository = mechanicRepository;

        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.mechanicRepository.count() > 0;
    }

    @Override
    public String readMechanicsFromFile() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "json", "mechanics.json");
        return Files.readString(path);
    }

    @Override
    public String importMechanics() throws IOException {
        String json = this.readMechanicsFromFile();
        ImportMechanicsDTO[] importMechanicsDTOs = this.gson.fromJson(json, ImportMechanicsDTO[].class);
        List<String> result = new ArrayList<>();

        for (ImportMechanicsDTO m : importMechanicsDTOs) {

            Set<ConstraintViolation<ImportMechanicsDTO>> validationErrors = validator.validate(m);
            if (validationErrors.isEmpty()) {

                Mechanic optMechanic = findByEmail(m.getEmail());
                if (optMechanic == null) {

                    Mechanic mechanicToSave = this.mapper.map(m, Mechanic.class);
                    this.mechanicRepository.save(mechanicToSave);
                    result.add(String.format("Successfully imported mechanic %s %s",
                            mechanicToSave.getFirstName(), mechanicToSave.getLastName()));
                } else {
                    result.add("Invalid mechanic");
                }
            } else {
                result.add("Invalid mechanic");
            }
        }
        return String.join(System.lineSeparator(), result);
    }

    @Override
    public Mechanic findByEmail(String email) {
        return this.mechanicRepository.findByEmail(email).orElse(null);
    }

    @Override
    public Mechanic findByFirstName(String firstName) {
        return this.mechanicRepository.findByFirstName(firstName).orElse(null);
    }
}
