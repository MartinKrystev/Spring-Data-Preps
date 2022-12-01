package softuni.exam.service;



import softuni.exam.models.Car;
import softuni.exam.models.dtos.ExportCarsWithPicsCountDTO;

import java.io.IOException;
import java.util.List;

public interface CarService {

    boolean areImported();

    String readCarsFileContent() throws IOException;
	
	String importCars() throws IOException;

    String getCarsOrderByPicturesCountThenByMake();

    Car getCarByMakeAndModelAndKilometers(String make, String model, Integer kilometers);

    Car getCarById(Long id);

    List<ExportCarsWithPicsCountDTO> findCarsWithPicturesCount();
}
