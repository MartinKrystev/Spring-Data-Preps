package exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exam.model.dtos.ImportLaptopDTO;
import exam.model.entities.Laptop;
import exam.model.entities.Shop;
import exam.model.entities.Town;
import exam.repository.LaptopRepository;
import exam.service.LaptopService;
import exam.service.ShopService;
import exam.service.TownService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class LaptopServiceImpl implements LaptopService {
    private final LaptopRepository laptopRepository;
    private final ShopService shopService;
    private final TownService townService;
    private final Gson gson;
    private final Validator validator;
    private final ModelMapper mapper;

    @Autowired
    public LaptopServiceImpl(LaptopRepository laptopRepository,
                             ShopService shopService,
                             TownService townService) {
        this.laptopRepository = laptopRepository;
        this.shopService = shopService;
        this.townService = townService;

        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.laptopRepository.count() > 0;
    }

    @Override
    public String readLaptopsFileContent() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "json", "laptops.json");
        return Files.readString(path);
    }

    @Override
    public String importLaptops() throws IOException {
        String json = this.readLaptopsFileContent();

        ImportLaptopDTO[] importLaptopDTOs = this.gson.fromJson(json, ImportLaptopDTO[].class);

        List<String> result = new ArrayList<>();
        for (ImportLaptopDTO i : importLaptopDTOs) {
            Set<ConstraintViolation<ImportLaptopDTO>> validationErrors = validator.validate(i);

            if (validationErrors.isEmpty()) {

                Laptop optLaptop = findByMacAddress(i.getMacAddress());
                if (optLaptop == null) {

                    Shop shopToAdd = this.shopService.findByName(i.getShop().getName());

                    Laptop laptopToAdd = this.mapper.map(i, Laptop.class);
                    laptopToAdd.setShop(shopToAdd);

                    this.laptopRepository.save(laptopToAdd);

                    String msg = String.format("Successfully imported Laptop %s - %.2f - %d - %d",
                            laptopToAdd.getMacAddress(), laptopToAdd.getCpuSpeed(), laptopToAdd.getRam(), laptopToAdd.getStorage());

                    result.add(msg);
                } else {
                    result.add("Invalid Laptop");
                }
            } else {
                result.add("Invalid Laptop");
            }
        }
        return String.join(System.lineSeparator(), result);
    }

    @Override
    public String exportBestLaptops() {
        StringBuilder sb = new StringBuilder();

        List<Laptop> laptops = findLaptopsWithShopAndTown();

        laptops.forEach(l -> {
            Shop shop = this.shopService.findByName(l.getShop().getName());
            Town town = this.townService.findByName(shop.getTown().getName());

            sb.append(String.format("Laptop - %s", l.getMacAddress())).append(System.lineSeparator());
            sb.append(String.format("*Cpu speed - %.2f", l.getCpuSpeed())).append(System.lineSeparator());
            sb.append(String.format("**Ram - %d", l.getRam())).append(System.lineSeparator());
            sb.append(String.format("***Storage - %d", l.getStorage())).append(System.lineSeparator());
            sb.append(String.format("****Price - %.2f",l.getPrice())).append(System.lineSeparator());
            sb.append(String.format("#Shop name - %s", l.getShop().getName())).append(System.lineSeparator());
            sb.append(String.format("##Town - %s", town.getName())).append(System.lineSeparator());
            sb.append(System.lineSeparator());
        });
        
        return sb.toString();
    }

    @Override
    public Laptop findByMacAddress(String macAddress) {
        return this.laptopRepository.findByMacAddress(macAddress).orElse(null);
    }

    @Override
    public List<Laptop> findLaptopsWithShopAndTown() {
        return this.laptopRepository.findLaptopsWithShopAndTown().orElse(null);
    }
}
