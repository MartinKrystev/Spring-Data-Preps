package softuni.exam.service;


import softuni.exam.models.Ticket;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface TicketService {

    boolean areImported();

    String readTicketsFileContent() throws IOException;
	
	String importTickets() throws FileNotFoundException, JAXBException;

    Ticket findBySerialNumber(String serialNumber);

}
