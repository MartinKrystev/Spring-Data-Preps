package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.Plane;
import softuni.exam.models.dtos.ImportBasePlaneDTO;
import softuni.exam.models.dtos.ImportPlanesRootDTO;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.service.PlaneService;

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
public class PlaneServiceImpl implements PlaneService {
    private final PlaneRepository planeRepository;
    private final Validator validator;
    private final ModelMapper mapper;

    @Autowired
    public PlaneServiceImpl(PlaneRepository planeRepository) {
        this.planeRepository = planeRepository;

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.planeRepository.count() > 0;
    }

    @Override
    public String readPlanesFileContent() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/xml/planes.xml"));
    }

    @Override
    public String importPlanes() throws FileNotFoundException, JAXBException {
        final FileReader fileReader = new FileReader(Path.of("src", "main", "resources", "files", "xml", "planes.xml").toFile());

        JAXBContext context = JAXBContext.newInstance(ImportPlanesRootDTO.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ImportPlanesRootDTO importPlanesDTOs = (ImportPlanesRootDTO) unmarshaller.unmarshal(fileReader);

        List<String> result = new ArrayList<>();
        for (ImportBasePlaneDTO p : importPlanesDTOs.getPlanes()) {

            Set<ConstraintViolation<ImportBasePlaneDTO>> validationErrors = validator.validate(p);
            if (validationErrors.isEmpty()) {

                Plane optPlane = findByRegisterNumber(p.getRegisterNumber());
                if (optPlane == null) {

                    Plane planeToSave = this.mapper.map(p, Plane.class);
                    this.planeRepository.save(planeToSave);

                    result.add(String.format("Successfully imported Plane %s",
                            planeToSave.getRegisterNumber()));
                } else {
                    result.add("Invalid Plane");
                }
            } else {
                result.add("Invalid Plane");
            }
        }
        return String.join(System.lineSeparator(), result);
    }

    @Override
    public Plane findByRegisterNumber(String registerNumber) {
        return this.planeRepository.findByRegisterNumber(registerNumber).orElse(null);
    }
}
