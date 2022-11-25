package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportForecastDTO;
import softuni.exam.models.dto.ImportForecastRootDTO;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.Forecast;
import softuni.exam.models.entity.enums.DayOfWeek;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.ForecastRepository;
import softuni.exam.service.ForecastService;
import softuni.exam.util.XmlParser;

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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ForecastServiceImpl implements ForecastService {
    private final Path path = Path.of("src", "main", "resources", "files", "xml", "forecasts.xml");
    private final ForecastRepository forecastRepository;
    private final CityRepository cityRepository;

    private final Validator validator;
    private final ModelMapper mapper;
    private final XmlParser xmlParser;

    @Autowired
    public ForecastServiceImpl(ForecastRepository forecastRepository,
                               CityRepository cityRepository,
                               XmlParser xmlParser) throws JAXBException {
        this.forecastRepository = forecastRepository;
        this.cityRepository = cityRepository;
        this.xmlParser = xmlParser;

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ModelMapper();

    }


    @Override
    public boolean areImported() {
        return this.forecastRepository.count() > 0;
    }

    @Override
    public String readForecastsFromFile() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/xml/forecasts.xml"));
    }

    @Override
    public String importForecasts() throws IOException, JAXBException {

        final FileReader fileReader = new FileReader(Path.of("src", "main", "resources", "files", "xml", "forecasts.xml").toFile());

        JAXBContext context = JAXBContext.newInstance(ImportForecastRootDTO.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ImportForecastRootDTO forecastsDTOs = (ImportForecastRootDTO) unmarshaller.unmarshal(fileReader);

        List<String> result = new ArrayList<>();
        for (ImportForecastDTO forecast : forecastsDTOs.getForecasts()) {
            Set<ConstraintViolation<ImportForecastDTO>> validationErrors = this.validator.validate(forecast);

            if (!validationErrors.isEmpty()) {
                result.add("Invalid forecast");
            } else {
                if (this.cityRepository.findFirstById(forecast.getCity()).isPresent()) {
                    City currCity = this.cityRepository.findFirstById(forecast.getCity()).get();

                    Forecast forecastToSave = this.mapper.map(forecast, Forecast.class);

                    forecastToSave.setCity(currCity);
                    this.forecastRepository.save(forecastToSave);
                    result.add(String.format("Successfully import forecast %s - %.2f",
                            forecastToSave.getDayOfWeek(), forecastToSave.getMaxTemperature()));
                }

            }
        }
        return String.join(System.lineSeparator(), result);
    }


    @Override
    public String exportForecasts() {

        Set<Forecast> forecasts = this.forecastRepository.findAllByDayOfWeekAndCity_PopulationLessThanOrderByMaxTemperatureDescIdAsc(DayOfWeek.SUNDAY, 150_000)
                .orElseThrow(NoSuchElementException::new);

        String result =  forecasts.stream()
                .map(f -> String.format("City: %s%n -min temperature: %.2f%n --max temperature: %.2f%n ---sunrise: %s%n ----sunset: %s%n",
                        f.getCity().getCityName(),
                        f.getMinTemperature(),
                        f.getMaxTemperature(),
                        f.getSunrise(),
                        f.getSunset()))
                .collect(Collectors.joining(System.lineSeparator()));

        System.out.println(result);

        return result;
    }
}
