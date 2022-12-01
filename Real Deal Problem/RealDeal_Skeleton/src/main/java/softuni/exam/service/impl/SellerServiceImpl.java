package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.Seller;
import softuni.exam.models.dtos.ImportSellersDTO;
import softuni.exam.models.dtos.ImportSellersRootDTO;
import softuni.exam.repository.SellerRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SellerServiceImpl implements SellerService {
    private final SellerRepository sellerRepository;
    private final Validator validator;
    private final ModelMapper mapper;

    @Autowired
    public SellerServiceImpl(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.sellerRepository.count() > 0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/xml/sellers.xml"));
    }

    @Override
    public String importSellers() throws IOException, JAXBException {
        FileReader fileReader = new FileReader(
                Path.of("src", "main", "resources", "files", "xml", "sellers.xml").toFile());

        JAXBContext context = JAXBContext.newInstance(ImportSellersRootDTO.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ImportSellersRootDTO sellersDTOs = (ImportSellersRootDTO) unmarshaller.unmarshal(fileReader);

        List<String> result = new ArrayList<>();
        for (ImportSellersDTO s : sellersDTOs.getSellers()) {

            Set<ConstraintViolation<ImportSellersDTO>> validationErrors = validator.validate(s);
            if (validationErrors.isEmpty()) {

                Seller optSeller = findByEmail(s.getEmail());
                if (optSeller == null) {

                    Seller sellerToSave = this.mapper.map(s, Seller.class);
                    this.sellerRepository.save(sellerToSave);
                    result.add(String.format("Successfully import seller Juanes - %s",
                            sellerToSave.getEmail()));
                } else {
                    result.add("Invalid seller");
                }
            } else {
                result.add("Invalid seller");
            }
        }
        return String.join(System.lineSeparator(), result);
    }

    @Override
    public Seller findByEmail(String email) {
        return this.sellerRepository.findByEmail(email).orElse(null);
    }

    @Override
    public Seller findById(Long id) {
        return this.sellerRepository.findById(id).orElse(null);
    }
}
