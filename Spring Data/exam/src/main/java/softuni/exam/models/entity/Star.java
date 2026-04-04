package softuni.exam.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stars")
@Getter
@Setter
@NoArgsConstructor
public class Star {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "light_years", nullable = false)
    private Double lightYears;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "star_type", nullable = false)
    private StarType starType;

    @ManyToOne
    @JoinColumn(name = "constellation_id")
    private Constellation constellation;

    @OneToMany(mappedBy = "observingStar")
    private List<Astronomer> observers = new ArrayList<>();
}