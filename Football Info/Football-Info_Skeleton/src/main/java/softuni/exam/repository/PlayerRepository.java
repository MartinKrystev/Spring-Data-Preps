package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.domain.entities.Player;
import softuni.exam.domain.entities.Team;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByFirstNameAndLastName(String firstName, String lastName);

    Optional<List<Player>> findAllByTeamOrderById(Team team);

    Optional<List<Player>> findAllBySalaryAfterOrderBySalaryDesc(BigDecimal biggerThan);
}
