package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportAgentsDTO;
import softuni.exam.models.entity.Agent;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.AgentRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.AgentService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AgentServiceImpl implements AgentService {
    private final AgentRepository agentRepository;
    private final TownRepository townRepository;
    private final Gson gson;
    private final Validator validator;
    private final ModelMapper mapper;

    @Autowired
    public AgentServiceImpl(AgentRepository agentRepository,
                            TownRepository townRepository) {
        this.agentRepository = agentRepository;
        this.townRepository = townRepository;

        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.agentRepository.count() > 0;
    }

    @Override
    public String readAgentsFromFile() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "json", "agents.json");
        return Files.readString(path);
    }

    @Override
    public String importAgents() throws IOException {
        String json = this.readAgentsFromFile();

        ImportAgentsDTO[] importAgentsDTOs = this.gson.fromJson(json, ImportAgentsDTO[].class);

        List<String> result = new ArrayList<>();
        for (ImportAgentsDTO importAgentsDTO : importAgentsDTOs) {
            Set<ConstraintViolation<ImportAgentsDTO>> validationErrors = this.validator.validate(importAgentsDTO);

            if (validationErrors.isEmpty()) {
                Optional<Agent> optAgent = this.agentRepository.findFirstByFirstName(importAgentsDTO.getFirstName());

                if (optAgent.isEmpty()) {
                    Agent agent = this.mapper.map(importAgentsDTO, Agent.class);
                    String msg = String.format("Successfully imported agent - %s %s",
                            agent.getFirstName(), agent.getLastName());

                    Optional<Town> optTown = this.townRepository.findFirstByTownName(importAgentsDTO.getTown());
                    if (optTown.isPresent()) {
                        agent.setTown(optTown.get());
                        this.agentRepository.save(agent);
                        result.add(msg);
                    } else {
                        result.add("Invalid agent");
                    }
                } else {
                    result.add("Invalid agent");
                }
            } else {
                result.add("Invalid agent");
            }
        }
        return String.join(System.lineSeparator(), result);
    }

    @Override
    public Agent findByFirstName(String firstName) {
        return agentRepository.findByFirstName(firstName).orElse(null);
    }
}
