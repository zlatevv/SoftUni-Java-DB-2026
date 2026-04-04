package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ConstellationImportDto;
import softuni.exam.models.entity.Constellation;
import softuni.exam.repository.ConstellationRepository;
import softuni.exam.service.ConstellationService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ConstellationServiceImpl implements ConstellationService {

    private static final String CONSTELLATIONS_FILE_PATH = "src/main/resources/files/json/constellations.json";
    private final ConstellationRepository constellationRepository;
    private final Gson gson;

    public ConstellationServiceImpl(ConstellationRepository constellationRepository) {
        this.constellationRepository = constellationRepository;
        this.gson = new Gson().newBuilder().create();
    }

    @Override
    public boolean areImported() {
        return constellationRepository.count() > 0;
    }

    @Override
    public String readConstellationsFromFile() throws IOException {
        return Files.readString(Path.of(CONSTELLATIONS_FILE_PATH));
    }

    @Override
    public String importConstellations() throws IOException {
        StringBuilder sb = new StringBuilder();
        ConstellationImportDto[] constellationImportDtos = this.gson.fromJson(readConstellationsFromFile(),
                ConstellationImportDto[].class);

        for (ConstellationImportDto constellationImportDto : constellationImportDtos) {
            boolean isValid = validate(constellationImportDto);

            if (isValid) {
                Constellation constellation = mapDtoToObject(constellationImportDto);
                constellationRepository.save(constellation);

                sb.append(String.format("Successfully imported constellation %s - %s",
                        constellation.getName(),
                        constellation.getDescription())).append(System.lineSeparator());
            } else {
                sb.append("Invalid constellation").append(System.lineSeparator());
            }
        }
        return sb.toString().trim();
    }

    private Constellation mapDtoToObject(ConstellationImportDto constellationImportDto) {
        Constellation constellation = new Constellation();

        constellation.setName(constellationImportDto.getName());
        constellation.setDescription(constellationImportDto.getDescription());

        return constellation;
    }

    private boolean validate(ConstellationImportDto constellationImportDto) {
        if (constellationImportDto.getName() == null || constellationImportDto.getDescription() == null) {
            return false;
        }

        if (constellationImportDto.getName().length() < 3 || constellationImportDto.getName().length() > 20) {
            return false;
        }
        if (constellationRepository.existsByName(constellationImportDto.getName())) {
            return false;
        }
        return constellationImportDto.getDescription().length() >= 5;
    }
}