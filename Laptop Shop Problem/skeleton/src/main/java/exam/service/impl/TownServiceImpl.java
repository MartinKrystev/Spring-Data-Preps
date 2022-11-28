package exam.service.impl;

import exam.model.dtos.ImportTownDTO;
import exam.model.dtos.ImportTownRootDTO;
import exam.model.entities.Town;
import exam.repository.TownRepository;
import exam.service.TownService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class TownServiceImpl implements TownService {
    private final TownRepository townRepository;
    private final Validator validator;
    private final ModelMapper mapper;

    @Autowired
    public TownServiceImpl(TownRepository townRepository) {
        this.townRepository = townRepository;

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/xml/towns.xml"));
    }

    @Override
    public String importTowns() throws JAXBException, FileNotFoundException {
        final FileReader fileReader = new FileReader(Path.of("src", "main", "resources", "files", "xml", "towns.xml").toFile());

        JAXBContext context = JAXBContext.newInstance(ImportTownRootDTO.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ImportTownRootDTO importPlayersDTOs = (ImportTownRootDTO) unmarshaller.unmarshal(fileReader);

        List<String> result = new ArrayList<>();
        for (ImportTownDTO t : importPlayersDTOs.getTowns()) {
            Set<ConstraintViolation<ImportTownDTO>> validationErrors = validator.validate(t);

            if (validationErrors.isEmpty()) {

                Town optTown = findByName(t.getName());
                if (optTown == null) {

                    Town townToSave = this.mapper.map(t, Town.class);
                    this.townRepository.saveAndFlush(townToSave);

                    String msg = String.format("Successfully imported Town %s",
                            townToSave.getName());
                    result.add(msg);
                } else {
                    result.add("Invalid town");
                }
            } else {
                result.add("Invalid town");
            }

        }
        return String.join(System.lineSeparator(), result);
    }

    @Override
    public Town findByName(String townName) {
        return this.townRepository.findByName(townName).orElse(null);
    }
}
