package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.Passenger;
import softuni.exam.models.Town;
import softuni.exam.models.dtos.ExportPassengersWithTicketsCountDTO;
import softuni.exam.models.dtos.ImportPassengersDTO;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.service.TownService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;
    private final TownService townService;
    private final Gson gson;
    private final Validator validator;
    private final ModelMapper mapper;

    @Autowired
    public PassengerServiceImpl(PassengerRepository passengerRepository,
                                TownService townService) {
        this.passengerRepository = passengerRepository;
        this.townService = townService;

        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.passengerRepository.count() > 0;
    }

    @Override
    public String readPassengersFileContent() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "json", "passengers.json");
        return Files.readString(path);
    }

    @Override
    public String importPassengers() throws IOException {
        String json = this.readPassengersFileContent();

        ImportPassengersDTO[] importPassengersDTOs = this.gson.fromJson(json, ImportPassengersDTO[].class);

        List<String> result = new ArrayList<>();
        for (ImportPassengersDTO p : importPassengersDTOs) {

            Set<ConstraintViolation<ImportPassengersDTO>> validationErrors = validator.validate(p);
            if (validationErrors.isEmpty()) {

                Passenger optPassenger = findByEmail(p.getEmail());
                if (optPassenger == null) {

                    Town optTown = this.townService.findByName(p.getTown());
                    if (optTown != null) {

                        Passenger passengerToSave = this.mapper.map(p, Passenger.class);
                        passengerToSave.setTown(optTown);

                        this.passengerRepository.save(passengerToSave);
                        result.add(String.format("Successfully imported Passenger %s - %s",
                                passengerToSave.getLastName(), passengerToSave.getEmail()));
                    } else {
                        result.add("Invalid Passenger");
                    }
                } else {
                    result.add("Invalid Passenger");
                }
            } else {
                result.add("Invalid Passenger");
            }
        }
        return String.join(System.lineSeparator(), result);
    }

    @Override
    public String getPassengersOrderByTicketsCountDescendingThenByEmail() {
        StringBuilder sb = new StringBuilder();

        List<ExportPassengersWithTicketsCountDTO> passengers = findPassengersWithTicketsCount();
        if (!passengers.isEmpty()) {

            for (ExportPassengersWithTicketsCountDTO p : passengers) {
                sb.append(String.format("Passenger %s %s", p.getFirstName(), p.getLastName())).append(System.lineSeparator());
                sb.append(String.format("\tEmail = %s", p.getEmail())).append(System.lineSeparator());
                sb.append(String.format("\tPhone - %s", p.getPhoneNumber())).append(System.lineSeparator());
                sb.append(String.format("\tNumber of tickets - %d", p.getTicketCount())).append(System.lineSeparator());
            }
        }
        return sb.toString();
    }

    @Override
    public Passenger findByEmail(String email) {
        return this.passengerRepository.findByEmail(email).orElse(null);
    }

    @Override
    public List<ExportPassengersWithTicketsCountDTO> findPassengersWithTicketsCount() {
        return this.passengerRepository.findPassengersWithTicketsCount().orElse(null);
    }
}
