package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ExportTasksDTO;
import softuni.exam.models.dto.ImportTasksDTO;
import softuni.exam.models.dto.ImportTasksRootDTO;
import softuni.exam.models.entity.*;
import softuni.exam.repository.TaskRepository;
import softuni.exam.service.CarService;
import softuni.exam.service.MechanicService;
import softuni.exam.service.PartService;
import softuni.exam.service.TaskService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final Validator validator;
    private final ModelMapper mapper;
    private final MechanicService mechanicService;
    private final PartService partService;
    private final CarService carService;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           MechanicService mechanicService,
                           PartService partService,
                           CarService carService) {
        this.taskRepository = taskRepository;
        this.mechanicService = mechanicService;
        this.partService = partService;
        this.carService = carService;

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.taskRepository.count() > 0;
    }

    @Override
    public String readTasksFileContent() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/xml/tasks.xml"));
    }

    @Override
    public String importTasks() throws IOException, JAXBException {
        final FileReader fileReader = new FileReader(Path.of("src", "main", "resources", "files", "xml", "tasks.xml").toFile());

        JAXBContext context = JAXBContext.newInstance(ImportTasksRootDTO.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ImportTasksRootDTO importTasksDTOs = (ImportTasksRootDTO) unmarshaller.unmarshal(fileReader);

        List<String> result = new ArrayList<>();
        for (ImportTasksDTO t : importTasksDTOs.getTasks()) {

            Set<ConstraintViolation<ImportTasksDTO>> validationErrors = validator.validate(t);
            if (validationErrors.isEmpty()) {

                Mechanic optMechanic = this.mechanicService.findByFirstName(t.getMechanic().getFirstName());
                if (optMechanic != null) {

                    Task optTask = findByMechanic(optMechanic);
                    if (optTask == null) {

                        Task taskToSave = this.mapper.map(t, Task.class);

                        Part partToAdd = this.partService.findById(taskToSave.getPart().getId());
                        Car carToAdd = this.carService.findById(taskToSave.getCar().getId());

                        if (carToAdd != null) {

                            taskToSave.setMechanic(optMechanic);
                            taskToSave.setPart(partToAdd);
                            taskToSave.setCar(carToAdd);

                            this.taskRepository.saveAndFlush(taskToSave);
                            result.add(String.format("Successfully imported task %.2f",
                                    taskToSave.getPrice()));
                        } else {
                            result.add("Invalid task");
                        }
                    } else {
                        result.add("Invalid task");
                    }
                } else {
                    result.add("Invalid task");
                }
            } else {
                result.add("Invalid task");
            }
        }
        return String.join(System.lineSeparator(), result);
    }

    @Override
    public String getCoupeCarTasksOrderByPrice() {
        StringBuilder sb = new StringBuilder();

        for (Task t : findAllByCar_CarTypeOrderByPriceDesc(CarType.coupe)) {

            sb.append(String.format("Car %s %s with %dkm", t.getCar().getCarMake(), t.getCar().getCarModel(), t.getCar().getKilometers()));
            ;
            sb.append(System.lineSeparator());
            sb.append(String.format("-Mechanic: %s %s - task №%d:", t.getMechanic().getFirstName(), t.getMechanic().getLastName(), t.getId()));
            sb.append(System.lineSeparator());
            sb.append(String.format("--Engine: %.1f", t.getCar().getEngine()));
            sb.append(System.lineSeparator());
            sb.append(String.format("---Price: %.2f$", t.getPrice()));
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    @Override
    public Task findByMechanic(Mechanic mechanic) {
        return this.taskRepository.findByMechanic(mechanic).orElse(null);
    }

    @Override
    public List<Task> findAllByCar_CarTypeOrderByPriceDesc(CarType carType) {
        return this.taskRepository.findAllByCar_CarTypeOrderByPriceDesc(carType).orElse(null);
    }
}
