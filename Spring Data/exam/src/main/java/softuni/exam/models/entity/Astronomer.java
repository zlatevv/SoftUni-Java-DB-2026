package softuni.exam.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "astronomers")
@Getter
@Setter
@NoArgsConstructor
public class Astronomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, columnDefinition = "VARCHAR(255)")
    private String firstName;

    @Column(name = "last_name", nullable = false, columnDefinition = "VARCHAR(255)")
    private String lastName;

    @Column(name = "salary", nullable = false, columnDefinition = "DOUBLE")
    private Double salary;

    @Column(name = "average_observation_hours", nullable = false, columnDefinition = "DOUBLE")
    private Double averageObservationHours;

    @Column(name = "birthday", columnDefinition = "DATE")
    private LocalDate birthday;

    @ManyToOne
    @JoinColumn(name = "observing_star_id", nullable = true)
    private Star observingStar;
}