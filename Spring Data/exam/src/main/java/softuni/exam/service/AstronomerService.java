package softuni.exam.service;

import java.io.IOException;

// TODO: Implement all methods
public interface AstronomerService {

    boolean areImported();

    String readAstronomersFromFile() throws IOException;

	String importAstronomers() throws IOException;

}
