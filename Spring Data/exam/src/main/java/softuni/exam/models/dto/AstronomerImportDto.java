package softuni.exam.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AstronomerImportDto {
    private Double averageObservationHours;
    private LocalDate birthday;
    private String firstName;
    private String lastName;
    private Double salary;
    private Long observingStarId;
}
