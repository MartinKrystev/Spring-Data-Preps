package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Offer;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    @Query("select o from Offer o " +
            "where o.apartment.apartmentType = 'three_rooms' " +
            "order by o.apartment.area desc, o.price asc")
    Optional<List<Offer>> findTheBestOffers();
}
