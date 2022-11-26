package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportOfferDTO;
import softuni.exam.models.dto.ImportOffersRootDTO;
import softuni.exam.models.entity.Offer;
import softuni.exam.repository.AgentRepository;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.AgentService;
import softuni.exam.service.ApartmentService;
import softuni.exam.service.OfferService;

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
public class OfferServiceImpl implements OfferService {
    private final OfferRepository offerRepository;
    private final AgentRepository agentRepository;
    private final AgentService agentService;
    private final ApartmentRepository apartmentRepository;
    private final ApartmentService apartmentService;
    private final Validator validator;
    private final ModelMapper mapper;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository,
                            AgentRepository agentRepository,
                            AgentService agentService,
                            ApartmentRepository apartmentRepository, ApartmentService apartmentService) {
        this.offerRepository = offerRepository;
        this.agentRepository = agentRepository;
        this.agentService = agentService;
        this.apartmentRepository = apartmentRepository;
        this.apartmentService = apartmentService;

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/xml/offers.xml"));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        FileReader fileReader = new FileReader(Path.of("src", "main", "resources", "files", "xml", "offers.xml")
                .toFile());

        JAXBContext context = JAXBContext.newInstance(ImportOffersRootDTO.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ImportOffersRootDTO offersDTOs = (ImportOffersRootDTO) unmarshaller.unmarshal(fileReader);

        List<String> result = new ArrayList<>();
        for (ImportOfferDTO offer : offersDTOs.getOffers()) {
            Set<ConstraintViolation<ImportOfferDTO>> validationErrors = this.validator.validate(offer);

            if (validationErrors.isEmpty()) {


                if (agentService.findByFirstName(offer.getAgent().getName()) != null) {


                    Offer offerToSave = this.mapper.map(offer, Offer.class);
                    offerToSave.setAgent(agentService.findByFirstName(offer.getAgent().getName()));
                    offerToSave.setApartment(apartmentService.findById(offer.getApartment().getId()));

                    this.offerRepository.save(offerToSave);

                    String msg = String.format("Successfully imported offer %.2f", offer.getPrice());
                    result.add(msg);
                } else {
                    result.add("Invalid offer");
                }
            } else {
                result.add("Invalid offer");
            }
        }

        return String.join(System.lineSeparator(), result);
    }

    @Override
    public String exportOffers() throws NoSuchFieldException {
        StringBuilder sb = new StringBuilder();

        List<Offer> offers = this.offerRepository.findTheBestOffers().orElseThrow(NoSuchFieldException::new);
        if (!offers.isEmpty()) {
            for (Offer o : offers) {
                sb.append(String.format("Agent %s %s with offer №%d:",
                        o.getAgent().getFirstName(), o.getAgent().getLastName(), o.getId()));
                sb.append(System.lineSeparator());
                sb.append(String.format("  -Apartment area: %.2f", o.getApartment().getArea()));
                sb.append(System.lineSeparator());
                sb.append(String.format("  --Town: %s", o.getApartment().getTown().getTownName()));
                sb.append(System.lineSeparator());
                sb.append(String.format("  ---Price: %.2f$", o.getPrice()));
                sb.append(System.lineSeparator());
            }
        }
        return sb.toString();
    }
}
