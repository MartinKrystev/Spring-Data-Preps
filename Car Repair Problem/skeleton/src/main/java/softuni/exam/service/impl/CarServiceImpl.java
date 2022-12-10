package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportCarsDTO;
import softuni.exam.models.dto.ImportCarsRootDTO;
import softuni.exam.models.entity.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;

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
import java.util.Set;

@Service
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final Validator validator;
    private final ModelMapper mapper;

    @Autowired
    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.carRepository.count() > 0;
    }

    @Override
    public String readCarsFromFile() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/xml/cars.xml"));
    }

    @Override
    public String importCars() throws IOException, JAXBException {
        final FileReader fileReader = new FileReader(Path.of("src", "main", "resources", "files", "xml", "cars.xml").toFile());

        JAXBContext context = JAXBContext.newInstance(ImportCarsRootDTO.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ImportCarsRootDTO importCarsDTOs = (ImportCarsRootDTO) unmarshaller.unmarshal(fileReader);

        List<String> result = new ArrayList<>();
        for (ImportCarsDTO c : importCarsDTOs.getCars()) {

            Set<ConstraintViolation<ImportCarsDTO>> validationErrors = validator.validate(c);
            if (validationErrors.isEmpty()) {

                Car optCar = findByPlateNumber(c.getPlateNumber());
                if (optCar == null) {

                    Car carToSave = this.mapper.map(c, Car.class);
                    this.carRepository.save(carToSave);
                    result.add(String.format("Successfully imported car %s - %s",
                            carToSave.getCarMake(), carToSave.getCarModel()));

                } else {
                    result.add("Invalid car");
                }
            } else {
                result.add("Invalid car");
            }
        }
        return String.join(System.lineSeparator(), result);
    }

    @Override
    public Car findByPlateNumber(String plateNumber) {
        return this.carRepository.findByPlateNumber(plateNumber).orElse(null);
    }

    @Override
    public Car findById(Long id) {
        return this.carRepository.findById(id).orElse(null);
    }
}
