package bg.softuni.bookshopsystem;

import bg.softuni.bookshopsystem.Enums.AgeRestriction;
import bg.softuni.bookshopsystem.Enums.EditionType;
import bg.softuni.bookshopsystem.models.Author;
import bg.softuni.bookshopsystem.models.Book;
import bg.softuni.bookshopsystem.models.Category;
import bg.softuni.bookshopsystem.repository.AuthorRepository;
import bg.softuni.bookshopsystem.repository.BookRepository;
import bg.softuni.bookshopsystem.repository.CategoryRepository;
import bg.softuni.bookshopsystem.services.AuthorService;
import bg.softuni.bookshopsystem.services.BookService;
import bg.softuni.bookshopsystem.services.CategoryService;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ConsoleRunner implements CommandLineRunner {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;
    private final BookService bookService;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    public ConsoleRunner(BookRepository bookRepository, CategoryRepository categoryRepository, AuthorRepository authorRepository, BookService bookService, AuthorService authorService, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.authorRepository = authorRepository;
        this.bookService = bookService;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    @Override
    public void run(String @NonNull ... args) throws IOException {
        seedDatabase();
        var books = bookService.getAllBooksAfter2000();

        System.out.println("Books after 2000: ");
        books.forEach(b -> System.out.println(b.getTitle() + " - " + b.getRelease_date()));
    }

    public void seedDatabase() throws IOException {
            if (authorRepository.count() == 0) {
                ClassPathResource authorsFile = new ClassPathResource("files/authors.txt");
                List<String> lines = Files.readAllLines(Path.of(authorsFile.getURI()));
                for (String line : lines) {
                    if (line.isBlank()) continue;
                    Author author = new Author();
                    String[] tokens = line.split("\\s+");
                    author.setFirst_name(tokens[0]);
                    author.setLast_name(tokens[1]);
                    authorRepository.save(author);
                }
            }
            if (categoryRepository.count() == 0) {
                ClassPathResource categoryFile = new ClassPathResource("files/categories.txt");
                List<String> lines = Files.readAllLines(Path.of(categoryFile.getURI()));
                for (String line : lines) {
                    if (line.isBlank()) continue;
                    Category category = new Category();
                    category.setName(line.trim());
                    categoryRepository.save(category);
                }
            }

            if (bookRepository.count() == 0) {
                ClassPathResource booksFile = new ClassPathResource("files/books.txt");
                List<String> lines = Files.readAllLines(Path.of(booksFile.getURI()));
                for (String line : lines) {
                    if (line.isBlank()) continue;
                    String[] data = line.split("\\s+");

                    EditionType editionType = EditionType.values()[Integer.parseInt(data[0])];
                    LocalDate releaseDate = LocalDate.parse(data[1], DateTimeFormatter.ofPattern("d/M/yyyy"));
                    int copies = Integer.parseInt(data[2]);
                    BigDecimal price = new BigDecimal(data[3]);
                    AgeRestriction ageRestriction = AgeRestriction.values()[Integer.parseInt(data[4])];
                    String title = Arrays.stream(data).skip(5).collect(Collectors.joining(" "));

                    Author randomAuthor = authorService.getRandomAuthor();
                    Set<Category> randomCategories = categoryService.getRandomCategories();

                    Book book = new Book(title, editionType, price, releaseDate,
                            ageRestriction, randomAuthor, randomCategories, copies);

                    bookRepository.save(book);
                }
            }
        }
}
