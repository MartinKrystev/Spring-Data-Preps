package softuni.exam.service;

import softuni.exam.domain.entities.Player;
import softuni.exam.domain.entities.Team;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface PlayerService {
    String importPlayers() throws IOException;

    boolean areImported();

    String readPlayersJsonFile() throws IOException;

    String exportPlayersWhereSalaryBiggerThan();

    String exportPlayersInATeam();

    Player findByFirstNameAndLastName(String firstName, String lastName);

    List<Player> findAllByTeamOrderById(Team team);

    List<Player> findAllBySalaryAfterOrderBySalaryDesc(BigDecimal biggerThan);
}
