package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.AstronomerImportDto;
import softuni.exam.models.entity.Astronomer;
import softuni.exam.models.entity.Star;
import softuni.exam.repository.AstronomerRepository;
import softuni.exam.repository.ConstellationRepository;
import softuni.exam.repository.StarRepository;
import softuni.exam.service.AstronomerService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class AstronomerServiceImpl implements AstronomerService {

    private static final String ASTRONOMERS_FILE_PATH = "src/main/resources/files/json/astronomers.json";
    private final StarRepository starRepository;
    private final AstronomerRepository astronomerRepository;
    private final Gson gson;

    public AstronomerServiceImpl(StarRepository starRepository, AstronomerRepository astronomerRepository) {
        this.starRepository = starRepository;
        this.astronomerRepository = astronomerRepository;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, context) ->
                        LocalDate.parse(json.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .create();
    }

    @Override
    public boolean areImported() {
        return astronomerRepository.count() > 0;
    }

    @Override
    public String readAstronomersFromFile() throws IOException {
        return Files.readString(Path.of(ASTRONOMERS_FILE_PATH));
    }

    @Override
    public String importAstronomers() throws IOException {
        StringBuilder sb = new StringBuilder();
        AstronomerImportDto[] astronomerImportDtos = this.gson.fromJson(readAstronomersFromFile(),
                AstronomerImportDto[].class);

        for (AstronomerImportDto dto : astronomerImportDtos) {
            boolean isValid = validate(dto);

            if (!isValid) {
                sb.append("Invalid astronomer").append(System.lineSeparator());
                continue;
            }

            Optional<Star> star = starRepository.findById(dto.getObservingStarId());

            if (star.isEmpty()) {
                sb.append("Invalid astronomer").append(System.lineSeparator());
                continue;
            }

            Astronomer astronomer = new Astronomer();
            astronomer.setFirstName(dto.getFirstName());
            astronomer.setLastName(dto.getLastName());
            astronomer.setAverageObservationHours(dto.getAverageObservationHours());
            astronomer.setBirthday(dto.getBirthday());
            astronomer.setSalary(dto.getSalary());
            astronomer.setObservingStar(star.get());

            astronomerRepository.save(astronomer);
            sb.append(String.format("Successfully imported astronomer %s %s - %.2f",
                            astronomer.getFirstName(), astronomer.getLastName(), astronomer.getAverageObservationHours()))
                    .append(System.lineSeparator());
        }
        return sb.toString().trim();
    }

    private boolean validate(AstronomerImportDto dto) {
        if (dto.getFirstName() == null || dto.getLastName() == null) {
            return false;
        }

        if (dto.getFirstName().length() < 2 || dto.getFirstName().length() > 30) {
            return false;
        }

        if (dto.getLastName().length() < 2 || dto.getLastName().length() > 30) {
            return false;
        }

        if (dto.getSalary() == null || dto.getSalary() < 15000.00) {
            return false;
        }

        if (this.astronomerRepository.existsByFirstNameAndLastName(dto.getFirstName(), dto.getLastName())) {
            return false;
        }

        return dto.getAverageObservationHours() != null && !(dto.getAverageObservationHours() < 500.00);
    }
}