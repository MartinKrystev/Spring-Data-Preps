package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportPartsDTO;
import softuni.exam.models.entity.Part;
import softuni.exam.repository.PartRepository;
import softuni.exam.service.PartService;

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
public class PartServiceImpl implements PartService {
    private final PartRepository partRepository;
    private final Gson gson;
    private final Validator validator;
    private final ModelMapper mapper;

    @Autowired
    public PartServiceImpl(PartRepository partRepository) {
        this.partRepository = partRepository;

        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.partRepository.count() > 0;
    }

    @Override
    public String readPartsFileContent() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "json", "parts.json");
        return Files.readString(path);
    }

    @Override
    public String importParts() throws IOException {
        String json = this.readPartsFileContent();
        ImportPartsDTO[] importPartsDTOs = this.gson.fromJson(json, ImportPartsDTO[].class);
        List<String> result = new ArrayList<>();

        for (ImportPartsDTO p : importPartsDTOs) {

            Set<ConstraintViolation<ImportPartsDTO>> validationErrors = validator.validate(p);
            if (validationErrors.isEmpty()) {

                Part optPart = findByPartName(p.getPartName());
                if (optPart == null) {

                    Part partToSave = this.mapper.map(p, Part.class);
                    this.partRepository.save(partToSave);
                    result.add(String.format("Successfully imported part %s - %.2f",
                            partToSave.getPartName(), partToSave.getPrice()));
                } else {
                    result.add("Invalid part");
                }
            } else {
                result.add("Invalid part");
            }
        }
        return String.join(System.lineSeparator(), result);
    }

    @Override
    public Part findByPartName(String partName) {
        return this.partRepository.findByPartName(partName).orElse(null);
    }

    @Override
    public Part findById(Long id) {
        return this.partRepository.findById(id).orElse(null);
    }
}
