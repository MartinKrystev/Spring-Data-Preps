package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.Car;
import softuni.exam.models.dtos.ExportCarsWithPicsCountDTO;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    Optional<Car> getCarByMakeAndModelAndKilometers(String make, String model, Integer kilometers);

    Optional<Car> getCarById(Long id);

    @Query("select new softuni.exam.models.dtos.ExportCarsWithPicsCountDTO(" +
            "c.make, c.model, c.kilometers, c.registeredOn, size(c.pictures)) " +
            "from Car c " +
            "order by size(c.pictures) desc, c.make")
    Optional<List<ExportCarsWithPicsCountDTO>> findCarsWithPicturesCount();
}
