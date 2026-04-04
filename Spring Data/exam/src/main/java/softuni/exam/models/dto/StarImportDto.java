package softuni.exam.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.exam.models.entity.StarType;

@Getter
@Setter
@NoArgsConstructor
public class StarImportDto {
    private String description;
    private Double lightYears;
    private String name;
    private StarType starType;
    private Long constellation;
}
