package bg.softuni.bookshopsystem.services;

import bg.softuni.bookshopsystem.models.Author;
import bg.softuni.bookshopsystem.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author getRandomAuthor() {
        long randomId = java.util.concurrent.ThreadLocalRandom.current()
                .nextLong(1, authorRepository.count() + 1);

        return authorRepository.findById(randomId).orElse(null);
    }

    @Override
    public List<Author> getAuthorsWithBookBefore1990() {

        return List.of();
    }

}
