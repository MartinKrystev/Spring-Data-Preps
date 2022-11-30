package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.Passenger;
import softuni.exam.models.Plane;
import softuni.exam.models.Ticket;
import softuni.exam.models.Town;
import softuni.exam.models.dtos.ImportTicketsDTO;
import softuni.exam.models.dtos.ImportTicketsRootDTO;
import softuni.exam.repository.TicketRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.service.PlaneService;
import softuni.exam.service.TicketService;
import softuni.exam.service.TownService;

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
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final TownService townService;
    private final PassengerService passengerService;
    private final PlaneService planeService;
    private final Validator validator;
    private final ModelMapper mapper;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository,
                             TownService townService,
                             PassengerService passengerService,
                             PlaneService planeService) {
        this.ticketRepository = ticketRepository;
        this.townService = townService;
        this.passengerService = passengerService;
        this.planeService = planeService;

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.ticketRepository.count() > 0;
    }

    @Override
    public String readTicketsFileContent() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/xml/tickets.xml"));
    }

    @Override
    public String importTickets() throws FileNotFoundException, JAXBException {
        final FileReader fileReader = new FileReader(Path.of("src", "main", "resources", "files", "xml", "tickets.xml").toFile());

        JAXBContext context = JAXBContext.newInstance(ImportTicketsRootDTO.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ImportTicketsRootDTO importTicketsDTOs = (ImportTicketsRootDTO) unmarshaller.unmarshal(fileReader);

        List<String> result = new ArrayList<>();
        for (ImportTicketsDTO t : importTicketsDTOs.getTickets()) {

            Set<ConstraintViolation<ImportTicketsDTO>> validationErrors = validator.validate(t);
            if (validationErrors.isEmpty()) {

                Ticket optTicket = findBySerialNumber(t.getSerialNumber());
                if (optTicket == null) {

                    Town optFromTown = this.townService.findByName(t.getFromTown().getName());
                    Town optToTown = this.townService.findByName(t.getToTown().getName());
                    Passenger optPassenger = this.passengerService.findByEmail(t.getPassenger().getEmail());
                    Plane optPlane = this.planeService.findByRegisterNumber(t.getPlane().getRegisterNumber());

                    if (optFromTown != null && optToTown != null && optPassenger != null && optPlane != null) {

                        Ticket ticketToSave = this.mapper.map(t, Ticket.class);

                        ticketToSave.setFromTown(optFromTown);
                        ticketToSave.setToTown(optToTown);
                        ticketToSave.setPassenger(optPassenger);
                        ticketToSave.setPlane(optPlane);

                        this.ticketRepository.save(ticketToSave);

                        result.add(String.format("Successfully imported Ticket %s - %s",
                                ticketToSave.getFromTown().getName(), ticketToSave.getToTown().getName()));
                    } else {
                        result.add("Invalid Ticket");
                    }
                } else {
                    result.add("Invalid Ticket");
                }
            } else {
                result.add("Invalid Ticket");
            }
        }
        return String.join(System.lineSeparator(), result);
    }

    @Override
    public Ticket findBySerialNumber(String serialNumber) {
        return this.ticketRepository.findBySerialNumber(serialNumber).orElse(null);
    }
}
