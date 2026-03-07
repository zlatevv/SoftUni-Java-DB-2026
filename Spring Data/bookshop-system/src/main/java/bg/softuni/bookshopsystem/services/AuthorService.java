package bg.softuni.bookshopsystem.services;

import bg.softuni.bookshopsystem.models.Author;

import java.util.List;

public interface AuthorService {
    Author getRandomAuthor();
    List<Author> getAuthorsWithBookBefore1990();
}
