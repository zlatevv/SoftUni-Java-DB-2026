package bg.softuni.bookshopsystem.services;

import bg.softuni.bookshopsystem.models.Category;

import java.util.Set;

public interface CategoryService {
    Set<Category> getRandomCategories();
}
