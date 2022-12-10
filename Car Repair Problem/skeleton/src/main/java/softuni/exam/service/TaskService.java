package softuni.exam.service;

import softuni.exam.models.dto.ExportTasksDTO;
import softuni.exam.models.entity.CarType;
import softuni.exam.models.entity.Mechanic;
import softuni.exam.models.entity.Task;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

public interface TaskService {

    boolean areImported();

    String readTasksFileContent() throws IOException;

    String importTasks() throws IOException, JAXBException;

    String getCoupeCarTasksOrderByPrice();

    Task findByMechanic(Mechanic mechanic);

    List<Task> findAllByCar_CarTypeOrderByPriceDesc(CarType carType);
}
