package bg.softuni.bookshopsystem.services;

import bg.softuni.bookshopsystem.models.Category;
import bg.softuni.bookshopsystem.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public Set<Category> getRandomCategories() {
        Set<Category> categories = new HashSet<>();
        int randomCount = java.util.concurrent.ThreadLocalRandom.current().nextInt(1, 4);
        long totalCategories = categoryRepository.count();

        Set<Long> uniqueCategoryIds = new HashSet<>();
        while (uniqueCategoryIds.size() < randomCount) {
            long randomId = java.util.concurrent.ThreadLocalRandom.current().nextLong(1, totalCategories + 1);
            uniqueCategoryIds.add(randomId);
        }

        for (Long id : uniqueCategoryIds) {
            categoryRepository.findById(Math.toIntExact(id)).ifPresent(categories::add);
        }

        return categories;
    }
}
