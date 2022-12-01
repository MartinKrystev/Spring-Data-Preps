package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.Car;
import softuni.exam.models.Picture;
import softuni.exam.models.dtos.ImportCarsDTO;
import softuni.exam.models.dtos.ImportPicturesDTO;
import softuni.exam.repository.PictureRepository;
import softuni.exam.service.CarService;
import softuni.exam.service.PictureService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class PictureServiceImpl implements PictureService {
    private final PictureRepository pictureRepository;
    private final CarService carService;
    private final Gson gson;
    private final Validator validator;
    private final ModelMapper mapper;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository,
                              CarService carService) {
        this.pictureRepository = pictureRepository;
        this.carService = carService;

        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesFromFile() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "json", "pictures.json");
        return Files.readString(path);
    }

    @Override
    public String importPictures() throws IOException {

        String json = this.readPicturesFromFile();

        ImportPicturesDTO[] importPicturesDTOs = this.gson.fromJson(json, ImportPicturesDTO[].class);
        addDateTimeConverter();

        List<String> result = new ArrayList<>();
        for (ImportPicturesDTO p : importPicturesDTOs) {

            Set<ConstraintViolation<ImportPicturesDTO>> validationErrors = validator.validate(p);
            if (validationErrors.isEmpty()) {

                Picture optPicture = getByName(p.getName());
                Car carToAdd = this.carService.getCarById(Long.valueOf(p.getCar()));
                if (optPicture == null && carToAdd != null) {

                    Picture pictureToSave = this.mapper.map(p, Picture.class);
                    pictureToSave.setCar(carToAdd);

                    this.pictureRepository.save(pictureToSave);
                    result.add(String.format("Successfully import picture - %s",
                            pictureToSave.getName()));
                } else {
                    result.add("Invalid picture");
                }
            } else {
                result.add("Invalid picture");
            }
        }
        return String.join(System.lineSeparator(), result);
    }

    @Override
    public Picture getByName(String name) {
        return this.pictureRepository.getByName(name).orElse(null);
    }

    private void addDateTimeConverter() {
        this.mapper.addConverter(new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(MappingContext<String, LocalDateTime> mappingContext) {
                LocalDateTime parse = LocalDateTime.parse(mappingContext.getSource(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                return parse;
            }
        });
    }
}
