package com.example.football.service.impl;

import com.example.football.models.dto.ImportTeamsDTO;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.repository.TeamRepository;
import com.example.football.service.TeamService;
import com.example.football.service.TownService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final TownService townService;
    private final Gson gson;
    private final Validator validator;
    private final ModelMapper mapper;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository,
                           TownService townService) {
        this.teamRepository = teamRepository;
        this.townService = townService;

        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.teamRepository.count() > 0;
    }

    @Override
    public String readTeamsFileContent() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "json", "teams.json");
        return Files.readString(path);
    }

    @Override
    public String importTeams() throws IOException {
        String json = this.readTeamsFileContent();
        ImportTeamsDTO[] importTeamsDTOs = this.gson.fromJson(json, ImportTeamsDTO[].class);
        List<String> result = new ArrayList<>();

        for (ImportTeamsDTO importTeamsDTO : importTeamsDTOs) {
            Set<ConstraintViolation<ImportTeamsDTO>> validationErrors = validator.validate(importTeamsDTO);

            if (validationErrors.isEmpty()) {

                if (findByName(importTeamsDTO.getTownName()) == null) {

                    Team teamToAdd = this.mapper.map(importTeamsDTO, Team.class);
                    Town TownToAdd = this.townService.findByName(importTeamsDTO.getTownName());

                    teamToAdd.setTown(TownToAdd);
                    this.teamRepository.save(teamToAdd);

                    String msg = String.format("Successfully imported Team %s - %d",
                            teamToAdd.getName(), teamToAdd.getFanBase());
                    result.add(msg);
                } else {
                    result.add("Invalid Team");
                }

            } else {
                result.add("Invalid Team");
            }
        }
        return String.join(System.lineSeparator(), result);
    }

    @Override
    public Team findByName(String name) {
        return this.teamRepository.findByName(name).orElse(null);
    }
}
