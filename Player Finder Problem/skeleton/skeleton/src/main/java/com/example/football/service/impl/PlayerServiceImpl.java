package com.example.football.service.impl;

import com.example.football.models.dto.ImportPlayerDTO;
import com.example.football.models.dto.ImportPlayerRootDTO;
import com.example.football.models.entity.Player;
import com.example.football.models.entity.Stat;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.repository.PlayerRepository;
import com.example.football.service.PlayerService;
import com.example.football.service.StatService;
import com.example.football.service.TeamService;
import com.example.football.service.TownService;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final TeamService teamService;
    private final TownService townService;
    private final StatService statService;
    private final Validator validator;
    private final ModelMapper mapper;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository,
                             TeamService teamService,
                             TownService townService,
                             StatService statService) {
        this.playerRepository = playerRepository;
        this.teamService = teamService;
        this.townService = townService;
        this.statService = statService;

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.playerRepository.count() > 0;
    }

    @Override
    public String readPlayersFileContent() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/xml/players.xml"));
    }

    @Override
    public String importPlayers() throws FileNotFoundException, JAXBException {
        final FileReader fileReader = new FileReader(Path.of("src", "main", "resources", "files", "xml", "players.xml").toFile());

        JAXBContext context = JAXBContext.newInstance(ImportPlayerRootDTO.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ImportPlayerRootDTO importPlayersDTOs = (ImportPlayerRootDTO) unmarshaller.unmarshal(fileReader);

        List<String> result = new ArrayList<>();
        for (ImportPlayerDTO player : importPlayersDTOs.getPlayers()) {
            Set<ConstraintViolation<ImportPlayerDTO>> validationErrors = validator.validate(player);

            if (validationErrors.isEmpty()) {

                if (findByEmail(player.getEmail()) == null) {

                    Team teamToAdd = this.teamService.findByName(player.getTeam().getName());
                    Town townToAdd = this.townService.findByName(player.getTown().getName());
                    Stat statToAdd = this.statService.findById(player.getStat().getId());

                    if (teamToAdd != null && townToAdd != null && statToAdd != null) {
                        Player playerToAdd = this.mapper.map(player, Player.class);

                        playerToAdd.setTeam(teamToAdd);
                        playerToAdd.setTown(townToAdd);
                        playerToAdd.setStat(statToAdd);
                        this.playerRepository.saveAndFlush(playerToAdd);

                        String msg = String.format("Successfully imported Player %s %s - %s",
                                playerToAdd.getFirstName(), playerToAdd.getLastName(), playerToAdd.getPosition().toString());

                        result.add(msg);
                    } else {
                        result.add("Invalid Player");
                    }
                } else {
                    result.add("Invalid Player");
                }
            } else {
                result.add("Invalid Player");
            }
        }
        return String.join(System.lineSeparator(), result);
    }

    @Override
    public String exportBestPlayers() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse("1995-01-01", df);
        LocalDate endDate = LocalDate.parse("2003-01-01", df);

        StringBuilder sb = new StringBuilder();

        List<Player> theBestPlayers = getTheBestPlayers(endDate, startDate);
        for (Player p : theBestPlayers) {
            sb.append(String.format("Player - %s %s", p.getFirstName(), p.getLastName()));
            sb.append(System.lineSeparator());
            sb.append(String.format(String.format("\tPosition - %s", p.getPosition())));
            sb.append(System.lineSeparator());
            sb.append(String.format("\tTeam - %s", p.getTeam().getName()));
            sb.append(System.lineSeparator());
            sb.append(String.format("\tStadium - %s", p.getTeam().getStadiumName()));
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }

    @Override
    public Player findByEmail(String email) {
        return this.playerRepository.findByEmail(email).orElse(null);
    }

    @Override
    public List<Player> getTheBestPlayers(LocalDate endDate, LocalDate startDate) {
        return this.playerRepository.getTheBestPlayers(endDate, startDate);
    }
}
