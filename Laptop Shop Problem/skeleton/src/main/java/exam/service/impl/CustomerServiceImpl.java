package exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exam.model.dtos.ImportCustomerDTO;
import exam.model.entities.Customer;
import exam.model.entities.Town;
import exam.repository.CustomerRepository;
import exam.service.CustomerService;
import exam.service.TownService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final TownService townService;
    private final Gson gson;
    private final Validator validator;
    private final ModelMapper mapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository,
                               TownService townService) {
        this.customerRepository = customerRepository;
        this.townService = townService;

        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.customerRepository.count() > 0;
    }

    @Override
    public String readCustomersFileContent() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "json", "customers.json");
        return Files.readString(path);
    }

    @Override
    public String importCustomers() throws IOException {
        String json = this.readCustomersFileContent();

        ImportCustomerDTO[] importCustomerDTOs = this.gson.fromJson(json, ImportCustomerDTO[].class);

        List<String> result = new ArrayList<>();
        for (ImportCustomerDTO c : importCustomerDTOs) {
            Set<ConstraintViolation<ImportCustomerDTO>> validationErrors = validator.validate(c);

            if (validationErrors.isEmpty()) {

                Customer optCustomer = findByEmail(c.getEmail());
                if (optCustomer == null) {

                    Town townToAdd = this.townService.findByName(c.getTown().getName());

                    this.mapper.addConverter(new Converter<String, LocalDate>() {
                        @Override
                        public LocalDate convert(MappingContext<String, LocalDate> mappingContext) {
                            return LocalDate.parse(mappingContext.getSource(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        }
                    });

                    Customer customerToAdd = this.mapper.map(c, Customer.class);
                    customerToAdd.setTown(townToAdd);

                    this.customerRepository.saveAndFlush(customerToAdd);

                    String msg = String.format("Successfully imported Customer %s %s - %s",
                            customerToAdd.getFirstName(), customerToAdd.getLastName(), customerToAdd.getEmail());
                    result.add(msg);
                } else {
                    result.add("Invalid Customer");
                }
            } else {
                result.add("Invalid Customer");
            }
        }
        return String.join(System.lineSeparator(), result);
    }

    @Override
    public Customer findByEmail(String email) {
        return this.customerRepository.findByEmail(email).orElse(null);
    }
}
