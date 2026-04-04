package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.StarImportDto;
import softuni.exam.models.entity.Constellation;
import softuni.exam.models.entity.Star;
import softuni.exam.repository.ConstellationRepository;
import softuni.exam.repository.StarRepository;
import softuni.exam.service.StarService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class StarServiceImpl implements StarService {

    private static final String STARS_FILE_PATH = "src/main/resources/files/json/stars.json";
    private final StarRepository starRepository;
    private final ConstellationRepository constellationRepository;
    private final Gson gson;

    public StarServiceImpl(StarRepository starRepository, ConstellationRepository constellationRepository) {
        this.starRepository = starRepository;
        this.constellationRepository = constellationRepository;
        this.gson = new Gson().newBuilder().create();
    }

    @Override
    public boolean areImported() {
        return starRepository.count() > 0;
    }

    @Override
    public String readStarsFileContent() throws IOException {
        return Files.readString(Path.of(STARS_FILE_PATH));
    }

    @Override
    public String importStars() throws IOException {
        StringBuilder sb = new StringBuilder();
        StarImportDto[] starImportDtos = gson.fromJson(readStarsFileContent(), StarImportDto[].class);

        for (StarImportDto starImportDto : starImportDtos) {
            boolean isValid = validate(starImportDto);

            if (!isValid) {
                sb.append("Invalid star").append(System.lineSeparator());
                continue;
            }

            Optional<Constellation> constellation = constellationRepository.findById(starImportDto.getConstellation());

            if (constellation.isEmpty()) {
                sb.append("Invalid star").append(System.lineSeparator());
                continue;
            }

            Star star = new Star();
            star.setName(starImportDto.getName());
            star.setLightYears(starImportDto.getLightYears());
            star.setStarType(starImportDto.getStarType());
            star.setConstellation(constellation.get());
            star.setDescription(starImportDto.getDescription());

            starRepository.save(star);
            sb.append(String.format("Successfully imported star %s - %.2f light years",
                    star.getName(),
                    star.getLightYears())).append(System.lineSeparator());
        }
        return sb.toString().trim();
    }

    private boolean validate(StarImportDto starImportDto) {
        if (starImportDto.getName() == null ||
                starImportDto.getName().length() < 2 ||
                starImportDto.getName().length() > 30) {
            return false;
        }

        if (starRepository.existsByName(starImportDto.getName())) {
            return false;
        }

        if (starImportDto.getLightYears() == null || starImportDto.getLightYears() <= 0) {
            return false;
        }

        if (starImportDto.getDescription() == null || starImportDto.getDescription().length() < 6) {
            return false;
        }

        return starImportDto.getStarType() != null;
    }

    @Override
    public String exportStars() {
        StringBuilder sb = new StringBuilder();
        List<Star> stars = starRepository.findEligibleStars();

        for (Star star : stars) {
            sb.append(String.format("Star: %s%n" +
                            "   *Distance: %.2f light years%n" +
                            "   **Description: %s%n" +
                            "   ***Constellation: %s%n",
                    star.getName(),
                    star.getLightYears(),
                    star.getDescription(),
                    star.getConstellation().getName()
            ));
        }
        return sb.toString().trim();
    }
}