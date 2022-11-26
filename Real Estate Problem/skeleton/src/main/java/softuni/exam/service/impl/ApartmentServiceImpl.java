package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportApartmentsDTO;
import softuni.exam.models.dto.ImportApartmentsRootDTO;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.ApartmentService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ApartmentServiceImpl implements ApartmentService {
    private final ApartmentRepository apartmentRepository;
    private final TownRepository townRepository;
    private final Validator validator;
    private final ModelMapper mapper;

    @Autowired
    public ApartmentServiceImpl(ApartmentRepository apartmentRepository,
                                TownRepository townRepository) {
        this.apartmentRepository = apartmentRepository;
        this.townRepository = townRepository;

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.apartmentRepository.count() > 0;
    }

    @Override
    public String readApartmentsFromFile() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/xml/apartments.xml"));
    }

    @Override
    public String importApartments() throws IOException, JAXBException {
        FileReader fileReader = new FileReader(Path.of("src", "main", "resources", "files", "xml", "apartments.xml")
                .toFile());

        JAXBContext context = JAXBContext.newInstance(ImportApartmentsRootDTO.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ImportApartmentsRootDTO apartmentsDTOs = (ImportApartmentsRootDTO) unmarshaller.unmarshal(fileReader);

        List<String> result = new ArrayList<>();
        for (ImportApartmentsDTO apartment : apartmentsDTOs.getApartments()) {
            Set<ConstraintViolation<ImportApartmentsDTO>> validationErrors = this.validator.validate(apartment);

            if (validationErrors.isEmpty()) {

                Optional<Town> optTown = this.townRepository.findFirstByTownName(apartment.getTown());
                if (optTown.isPresent()) {

                    Optional<Apartment> optApartment = this.apartmentRepository.findByTownAndArea(optTown.get(), apartment.getArea());
                    if (optApartment.isEmpty()) {

                        Apartment apartmentToSave = this.mapper.map(apartment, Apartment.class);
                        apartmentToSave.setTown(optTown.get());

                        this.apartmentRepository.save(apartmentToSave);
                        String msg = String.format("Successfully imported apartment %s - %.2f",
                        apartmentToSave.getApartmentType(), apartmentToSave.getArea());

                        result.add(msg);
                    } else {
                        result.add("Invalid apartment");
                    }
                } else {
                    result.add("Invalid apartment");
                }
            } else {
                result.add("Invalid apartment");
            }
        }
        return String.join(System.lineSeparator(), result);
    }

    @Override
    public Apartment findById(Long id) {
        return apartmentRepository.findById(id).orElse(null);
    }
}
