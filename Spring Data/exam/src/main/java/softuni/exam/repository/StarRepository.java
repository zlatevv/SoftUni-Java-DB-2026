package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Star;

import java.util.List;

@Repository
public interface StarRepository extends JpaRepository<Star,Long> {
    boolean existsByName(String name);

    @Query("SELECT s FROM Star s WHERE s.starType = 'RED_GIANT' AND s.observers IS EMPTY ORDER BY s.lightYears ASC")
    List<Star> findEligibleStars();
}