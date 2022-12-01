package softuni.exam.service;


import softuni.exam.models.Picture;

import java.io.IOException;

public interface PictureService {

    boolean areImported();

    String readPicturesFromFile() throws IOException;
	
	String importPictures() throws IOException;

    Picture getByName(String name);

}
