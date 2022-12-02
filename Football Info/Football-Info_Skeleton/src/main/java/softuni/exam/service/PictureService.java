package softuni.exam.service;

import softuni.exam.domain.entities.Picture;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface PictureService {
    String importPictures() throws FileNotFoundException, JAXBException;

    boolean areImported();

    String readPicturesXmlFile() throws IOException;

    Picture findByUrl(String url);
}
