package softuni.exam.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.Passenger;
import softuni.exam.models.dtos.ExportPassengersWithTicketsCountDTO;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassengerRepository  extends JpaRepository<Passenger, Long> {
    Optional<Passenger> findByEmail(String email);

    @Query("select new softuni.exam.models.dtos.ExportPassengersWithTicketsCountDTO(" +
            "p.firstName, p.lastName, p.email, p.phoneNumber, size(p.tickets)) " +
            "from Passenger p " +
            "group by p " +
            "order by size(p.tickets) desc, p.email")
    Optional<List<ExportPassengersWithTicketsCountDTO>> findPassengersWithTicketsCount();
}
//    @Query("select new softuni.exam.models.dtos.ExportPassengersWithTicketsCountDTO(" +
//            "p.firstName, p.lastName, p.email, p.phoneNumber, count(t.passenger.id)) " +
//            "from Passenger p join Ticket t " +
//            "group by t.passenger.id " +
//            "order by count(t.passenger.id) desc, p.email")