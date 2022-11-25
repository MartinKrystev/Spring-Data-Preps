package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportCountryDTO;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CountryService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;
    private final Gson gson;
    private final Validator validator;
    private final ModelMapper mapper;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.countryRepository.count() > 0;
    }

    @Override
    public String readCountriesFromFile() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "json", "countries.json");
        return Files.readString(path);
    }

    @Override
    public String importCountries() throws IOException {
        String json = this.readCountriesFromFile();

        ImportCountryDTO[] importCountryDTOs = this.gson.fromJson(json, ImportCountryDTO[].class);

        List<String> result = new ArrayList<>();
        for (ImportCountryDTO importCountryDTO : importCountryDTOs) {
            Set<ConstraintViolation<ImportCountryDTO>> validationErrors = this.validator.validate(importCountryDTO);

            if (validationErrors.isEmpty()) {
                Optional<Country> optCountry = this.countryRepository.findByName(importCountryDTO.getCountryName());

                if (optCountry.isPresent()) {
                    result.add("Invalid country");
                } else {
                    Country country = this.mapper.map(importCountryDTO, Country.class);

                    this.countryRepository.save(country);
                    String msg = String.format("Successfully imported country %s - %s",
                            country.getName(), country.getCurrency());

                    result.add(msg);
                }
            } else {
                result.add("Invalid country");
            }
        }
        return String.join(System.lineSeparator(), result);
    }
}
