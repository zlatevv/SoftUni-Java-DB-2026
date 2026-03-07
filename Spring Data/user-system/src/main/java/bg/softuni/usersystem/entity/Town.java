package bg.softuni.usersystem.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "towns")
public class Town {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "town_id")
    private Long townId;

    @Column(name = "town_name", length = 100)
    private String townName;

    @OneToMany(mappedBy = "town")
    private Set<User> users;
}
