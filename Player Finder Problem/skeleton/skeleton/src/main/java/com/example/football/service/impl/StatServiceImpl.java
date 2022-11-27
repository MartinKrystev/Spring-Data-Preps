package com.example.football.service.impl;

import com.example.football.models.dto.ImportStatsDTO;
import com.example.football.models.dto.ImportStatsRootDTO;
import com.example.football.models.entity.Stat;
import com.example.football.repository.StatRepository;
import com.example.football.service.StatService;
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
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;
    private final Validator validator;
    private final ModelMapper mapper;

    @Autowired
    public StatServiceImpl(StatRepository statRepository) {
        this.statRepository = statRepository;

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.statRepository.count() > 0;
    }

    @Override
    public String readStatsFileContent() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/xml/stats.xml"));
    }

    @Override
    public String importStats() throws FileNotFoundException, JAXBException {
        final FileReader fileReader = new FileReader(Path.of("src", "main", "resources", "files", "xml", "stats.xml").toFile());

        JAXBContext context = JAXBContext.newInstance(ImportStatsRootDTO.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ImportStatsRootDTO importStatsDTOs = (ImportStatsRootDTO) unmarshaller.unmarshal(fileReader);

        List<String> result = new ArrayList<>();

        for (ImportStatsDTO stat : importStatsDTOs.getStats()) {
            Set<ConstraintViolation<ImportStatsDTO>> validationErrors = this.validator.validate(stat);

            if (validationErrors.isEmpty()) {

                if (findByPassingAndShootingAndEndurance(stat.getPassing(), stat.getShooting(), stat.getEndurance()) == null) {

                    Stat statToAdd = this.mapper.map(stat, Stat.class);
                    this.statRepository.saveAndFlush(statToAdd);

                    String msg = String.format("Successfully imported Stat %.2f - %.2f - %.2f",
                            statToAdd.getShooting(), statToAdd.getPassing(), statToAdd.getEndurance());
                    result.add(msg);
                } else {
                    result.add("Invalid Stat");
                }
            } else {
                result.add("Invalid Stat");
            }
        }
        return String.join(System.lineSeparator(), result);
    }

    @Override
    public Stat findByPassingAndShootingAndEndurance(Double passing, Double shooting, Double endurance) {
        return this.statRepository.findByPassingAndShootingAndEndurance(passing, shooting, endurance).orElse(null);
    }

    @Override
    public Stat findById(Long id) {
        return this.statRepository.findById(id).orElse(null);
    }
}
