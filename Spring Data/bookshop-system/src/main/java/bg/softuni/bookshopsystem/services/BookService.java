package bg.softuni.bookshopsystem.services;

import bg.softuni.bookshopsystem.models.Book;

import java.util.List;

public interface BookService {
    List<Book> getAllBooksAfter2000();

}
