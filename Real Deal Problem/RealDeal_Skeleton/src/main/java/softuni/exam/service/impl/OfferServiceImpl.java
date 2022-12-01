package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.Car;
import softuni.exam.models.Offer;
import softuni.exam.models.Seller;
import softuni.exam.models.dtos.ImportOffersDTO;
import softuni.exam.models.dtos.ImportOffersRootDTO;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.CarService;
import softuni.exam.service.OfferService;
import softuni.exam.service.SellerService;

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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class OfferServiceImpl implements OfferService {
    private final OfferRepository offerRepository;
    private final CarService carService;
    private final SellerService sellerService;
    private final Validator validator;
    private final ModelMapper mapper;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository,
                            CarService carService,
                            SellerService sellerService) {
        this.offerRepository = offerRepository;
        this.carService = carService;
        this.sellerService = sellerService;

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
        FileReader fileReader = new FileReader(
                Path.of("src", "main", "resources", "files", "xml", "offers.xml").toFile());

        JAXBContext context = JAXBContext.newInstance(ImportOffersRootDTO.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ImportOffersRootDTO offersDTOs = (ImportOffersRootDTO) unmarshaller.unmarshal(fileReader);

        List<String> result = new ArrayList<>();
        for (ImportOffersDTO o : offersDTOs.getOffers()) {

            Set<ConstraintViolation<ImportOffersDTO>> validationErrors = validator.validate(o);
            if (validationErrors.isEmpty()) {

                Offer optOffer = findByDescriptionAndAddedOn(o.getDescription(), o.getAddedOn());
                Car optCar = this.carService.getCarById(o.getCar().getId());
                Seller optSeller = this.sellerService.findById(o.getSeller().getId());

                if (optOffer == null && optCar != null && optSeller != null) {

                    Offer offerToSave = this.mapper.map(o, Offer.class);
                    this.offerRepository.save(offerToSave);

                    result.add(String.format("Successfully import offer %s - %s",
                            offerToSave.getAddedOn(), offerToSave.getHasGoldStatus()));
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
    public Offer findByDescriptionAndAddedOn(String description, LocalDateTime addedOn) {
        return this.offerRepository.findByDescriptionAndAddedOn(description, addedOn).orElse(null);
    }
}
