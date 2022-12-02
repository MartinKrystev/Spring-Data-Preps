package softuni.exam.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dtos.ImportPlayersDTO;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Player;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.PlayerRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final PictureService pictureService;
    private final TeamService teamService;
    private final Gson gson;
    private final Validator validator;
    private final ModelMapper mapper;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository,
                             PictureService pictureService,
                             TeamService teamService) {
        this.playerRepository = playerRepository;
        this.pictureService = pictureService;
        this.teamService = teamService;

        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ModelMapper();
    }

    @Override
    public String importPlayers() throws IOException {
        String json = this.readPlayersJsonFile();
        ImportPlayersDTO[] importPlayersDTOs = this.gson.fromJson(json, ImportPlayersDTO[].class);
        List<String> result = new ArrayList<>();

        for (ImportPlayersDTO p : importPlayersDTOs) {

            Set<ConstraintViolation<ImportPlayersDTO>> validationErrors = validator.validate(p);
            if (validationErrors.isEmpty()) {

                Player optPlayer = findByFirstNameAndLastName(p.getFirstName(), p.getLastName());
                Picture optPicture = this.pictureService.findByUrl(p.getPicture().getUrl());
                Team optTown = this.teamService.findByName(p.getTeam().getName());
                if (optPlayer == null && optPicture != null && optTown != null) {

                    Player playerToSave = this.mapper.map(p, Player.class);
                    playerToSave.setPicture(optPicture);
                    playerToSave.setTeam(optTown);

                    this.playerRepository.save(playerToSave);
                    result.add(String.format("Successfully imported player: %s %s",
                            playerToSave.getFirstName(), playerToSave.getLastName()));
                } else {
                    result.add("Invalid player");
                }
            } else {
                result.add("Invalid player");
            }
        }
        return String.join(System.lineSeparator(), result);
    }

    @Override
    public boolean areImported() {
        return this.playerRepository.count() > 0;
    }

    @Override
    public String readPlayersJsonFile() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "json", "players.json");
        return Files.readString(path);
    }

    @Override
    public String exportPlayersWhereSalaryBiggerThan() {
        StringBuilder sb = new StringBuilder();

        BigDecimal biggerThan = new BigDecimal(100_000);
        List<Player> players = findAllBySalaryAfterOrderBySalaryDesc(biggerThan);

        players.forEach(p -> {
            sb.append(String.format("Player name: %s %s",p.getFirstName(), p.getLastName()));
            sb.append(System.lineSeparator());
            sb.append(String.format("\tNumber: %d", p.getNumber()));
            sb.append(System.lineSeparator());
            sb.append(String.format("\tSalary: %.0f", p.getSalary()));
            sb.append(System.lineSeparator());
            sb.append(String.format("\tTeam: %s", p.getTeam().getName()));
            sb.append(System.lineSeparator());
        });

        return sb.toString();
    }

    @Override
    public String exportPlayersInATeam() {
        StringBuilder sb = new StringBuilder();

        Team team = this.teamService.findByName("North Hub");
        List<Player> players = findAllByTeamOrderById(team);

            sb.append(String.format("Team: %s", team.getName()));
            sb.append(System.lineSeparator());

        players.forEach(p -> {
            sb.append(String.format("\tPlayer name: %s %s - %s",
                    p.getFirstName(), p.getLastName(), p.getPosition()));
            sb.append(System.lineSeparator());
            sb.append(String.format("\tNumber: %d", p.getNumber()));
            sb.append(System.lineSeparator());
        });

        return sb.toString();
    }

    @Override
    public Player findByFirstNameAndLastName(String firstName, String lastName) {
        return this.playerRepository.findByFirstNameAndLastName(firstName, lastName).orElse(null);
    }

    @Override
    public List<Player> findAllByTeamOrderById(Team team) {
        return this.playerRepository.findAllByTeamOrderById(team).orElse(null);
    }

    @Override
    public List<Player> findAllBySalaryAfterOrderBySalaryDesc(BigDecimal biggerThan) {
        return this.playerRepository.findAllBySalaryAfterOrderBySalaryDesc(biggerThan).orElse(null);
    }
}
