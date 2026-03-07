package bg.softuni.bookshopsystem.repository;

import bg.softuni.bookshopsystem.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
