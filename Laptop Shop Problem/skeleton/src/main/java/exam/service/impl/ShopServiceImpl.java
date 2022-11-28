package exam.service.impl;

import exam.model.dtos.ImportShopDTO;
import exam.model.dtos.ImportShopRootDTO;
import exam.model.entities.Shop;
import exam.model.entities.Town;
import exam.repository.ShopRepository;
import exam.service.ShopService;
import exam.service.TownService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class ShopServiceImpl implements ShopService {
    private final ShopRepository shopRepository;
    private final TownService townService;
    private final Validator validator;
    private final ModelMapper mapper;

    @Autowired
    public ShopServiceImpl(ShopRepository shopRepository,
                           TownService townService) {
        this.shopRepository = shopRepository;
        this.townService = townService;

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.shopRepository.count() > 0;
    }

    @Override
    public String readShopsFileContent() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/xml/shops.xml"));
    }

    @Override
    public String importShops() throws JAXBException, FileNotFoundException {
        final FileReader fileReader = new FileReader(Path.of("src", "main", "resources", "files", "xml", "shops.xml").toFile());

        JAXBContext context = JAXBContext.newInstance(ImportShopRootDTO.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ImportShopRootDTO importShopDTOs = (ImportShopRootDTO) unmarshaller.unmarshal(fileReader);

        List<String> result = new ArrayList<>();
        for (ImportShopDTO s : importShopDTOs.getShops()) {
            Set<ConstraintViolation<ImportShopDTO>> validationErrors = validator.validate(s);

            if (validationErrors.isEmpty()) {

                Shop optShop = findByName(s.getName());
                if (optShop == null) {

                    Town optTown = this.townService.findByName(s.getTown().getName());
                    Shop shopToSave = this.mapper.map(s, Shop.class);

                    shopToSave.setTown(optTown);

                    this.shopRepository.saveAndFlush(shopToSave);

                    String msg = String.format("Successfully imported Shop %s - %f",
                            shopToSave.getName(), shopToSave.getIncome());
                    result.add(msg);
                } else {
                    result.add("Invalid shop");
                }
            } else {
                result.add("Invalid shop");
            }


        }
        return String.join(System.lineSeparator(), result);
    }

    @Override
    public Shop findByName(String shopName) {
        return this.shopRepository.findByName(shopName).orElse(null);
    }
}
