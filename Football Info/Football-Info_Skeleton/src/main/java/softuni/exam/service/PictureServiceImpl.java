package softuni.exam.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dtos.ImportPicturesDTO;
import softuni.exam.domain.dtos.ImportPicturesRootDTO;
import softuni.exam.domain.entities.Picture;
import softuni.exam.repository.PictureRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class PictureServiceImpl implements PictureService {
    private final PictureRepository pictureRepository;
    private final Validator validator;
    private final ModelMapper mapper;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.mapper = new ModelMapper();
    }

    @Override
    public String importPictures() throws FileNotFoundException, JAXBException {
        final FileReader fileReader = new FileReader(Path.of("src", "main", "resources", "files", "xml", "pictures.xml").toFile());

        JAXBContext context = JAXBContext.newInstance(ImportPicturesRootDTO.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ImportPicturesRootDTO importPicturesDTOs = (ImportPicturesRootDTO) unmarshaller.unmarshal(fileReader);

        List<String> result = new ArrayList<>();
        for (ImportPicturesDTO p : importPicturesDTOs.getPictures()) {

            Set<ConstraintViolation<ImportPicturesDTO>> validationErrors = validator.validate(p);
            if (validationErrors.isEmpty()) {

                Picture optPicture = findByUrl(p.getUrl());
                if (optPicture == null) {

                    Picture pictureToSave = this.mapper.map(p, Picture.class);
                    this.pictureRepository.save(pictureToSave);
                    result.add(String.format("Successfully imported picture - %s",
                            pictureToSave.getUrl()));
                } else {
                    result.add("Invalid picture");
                }
            } else {
                result.add("Invalid picture");
            }
        }
        return String.join(System.lineSeparator(), result);
    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesXmlFile() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/xml/pictures.xml"));
    }

    @Override
    public Picture findByUrl(String url) {
        return this.pictureRepository.findByUrl(url).orElse(null);
    }

}
