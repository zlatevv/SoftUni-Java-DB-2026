package bg.softuni.bookshopsystem.services;

import bg.softuni.bookshopsystem.models.Book;
import bg.softuni.bookshopsystem.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getAllBooksAfter2000() {
        return bookRepository.findAllByReleaseDateAfter(LocalDate.of(2000, 12, 31));
    }
}
