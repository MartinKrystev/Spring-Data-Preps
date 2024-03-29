package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportCityDTO;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CityService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final Gson gson;
    private final Validator validator;
    private final ModelMapper mapper;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository, CountryRepository countryRepository) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;

        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.cityRepository.count() > 0;
    }

    @Override
    public String readCitiesFileContent() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "json", "cities.json");
        return Files.readString(path);
    }

    @Override
    public String importCities() throws IOException {
        String json = this.readCitiesFileContent();

        ImportCityDTO[] importCityDTOs = gson.fromJson(json, ImportCityDTO[].class);

        List<String> result = new ArrayList<>();
        for (ImportCityDTO importCityDTO : importCityDTOs) {
            Set<ConstraintViolation<ImportCityDTO>> validationErrors = this.validator.validate(importCityDTO);

            if (validationErrors.isEmpty()) {
                Optional<City> optCity = this.cityRepository.findByCityName(importCityDTO.getCityName());

                if (optCity.isPresent()) {
                    result.add("Invalid city");
                } else {

                    Optional<Country> country = this.countryRepository.findById(importCityDTO.getCountry());
                    if (country.isEmpty()) {
                        result.add("Invalid city");
                    } else {

                        City city = this.mapper.map(importCityDTO, City.class);
                        city.setCountry(country.get());

                        String msg = String.format("Successfully imported city %s - %d",
                                city.getCityName(), city.getPopulation());

                        result.add(msg);
                        this.cityRepository.save(city);
                    }
                }
            } else {
                result.add("Invalid city");
            }
        }

        return String.join(System.lineSeparator(), result);
    }
}
