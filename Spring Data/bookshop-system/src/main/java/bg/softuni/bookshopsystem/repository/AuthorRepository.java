package bg.softuni.bookshopsystem.repository;

import bg.softuni.bookshopsystem.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
