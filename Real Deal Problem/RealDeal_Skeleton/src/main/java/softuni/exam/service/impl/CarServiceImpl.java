package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.Car;
import softuni.exam.models.dtos.ExportCarsWithPicsCountDTO;
import softuni.exam.models.dtos.ImportCarsDTO;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final Validator validator;
    private final ModelMapper mapper;
    private final Gson gson;


    @Autowired
    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;

        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.carRepository.count() > 0;
    }

    @Override
    public String readCarsFileContent() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "json", "cars.json");
        return Files.readString(path);
    }

    @Override
    public String importCars() throws IOException {

        String json = this.readCarsFileContent();

        ImportCarsDTO[] importCarsDTOs = this.gson.fromJson(json, ImportCarsDTO[].class);

        addDateConverter();

        List<String> result = new ArrayList<>();
        for (ImportCarsDTO c : importCarsDTOs) {
            Set<ConstraintViolation<ImportCarsDTO>> validationErrors = validator.validate(c);

            if (validationErrors.isEmpty()) {
                Car optCar = getCarByMakeAndModelAndKilometers(c.getMake(), c.getModel(), c.getKilometers());

                if (optCar == null) {

                    Car carToSave = this.mapper.map(c, Car.class);

                    this.carRepository.save(carToSave);
                    result.add(String.format("Successfully imported car - %s - %s",
                            carToSave.getMake(), carToSave.getModel()));
                } else {
                    result.add("Invalid car");
                }
            } else {
                result.add("Invalid car");
            }
        }
        return String.join(System.lineSeparator(), result);
    }

    private void addDateConverter() {
        this.mapper.addConverter(new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(MappingContext<String, LocalDate> mappingContext) {
                return LocalDate.parse(mappingContext.getSource(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }
        });
    }

    @Override
    public String getCarsOrderByPicturesCountThenByMake() {
        StringBuilder sb = new StringBuilder();

        List<ExportCarsWithPicsCountDTO> cars = findCarsWithPicturesCount();
        if (!cars.isEmpty()) {
            
            for (ExportCarsWithPicsCountDTO c : cars) {
                sb.append(String.format("Car make - %s, model - %s", c.getMake(), c.getModel()));
                sb.append(System.lineSeparator());
                sb.append(String.format("\tKilometers = %d", c.getKilometers()));
                sb.append(System.lineSeparator());
                sb.append(String.format("\tRegistered on - %s", c.getRegisteredOn()));
                sb.append(System.lineSeparator());
                sb.append(String.format("\tNumbeof pictures - %d", c.getCountPictures()));
                sb.append(System.lineSeparator());
            }
        }
        return sb.toString();
    }

    @Override
    public Car getCarByMakeAndModelAndKilometers(String make, String model, Integer kilometers) {
        return this.carRepository.getCarByMakeAndModelAndKilometers(make, model, kilometers).orElse(null);
    }

    @Override
    public Car getCarById(Long id) {
        return this.carRepository.getCarById(id).orElse(null);
    }

    @Override
    public List<ExportCarsWithPicsCountDTO> findCarsWithPicturesCount() {
        return this.carRepository.findCarsWithPicturesCount().orElse(null);
    }
}
