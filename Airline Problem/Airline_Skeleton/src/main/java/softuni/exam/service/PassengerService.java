package softuni.exam.service;

import softuni.exam.models.Passenger;
import softuni.exam.models.dtos.ExportPassengersWithTicketsCountDTO;

import java.io.IOException;
import java.util.List;

public interface PassengerService {

    boolean areImported();

    String readPassengersFileContent() throws IOException;
	
	String importPassengers() throws IOException;

	String getPassengersOrderByTicketsCountDescendingThenByEmail();

    Passenger findByEmail(String email);

    List<ExportPassengersWithTicketsCountDTO> findPassengersWithTicketsCount();
}
