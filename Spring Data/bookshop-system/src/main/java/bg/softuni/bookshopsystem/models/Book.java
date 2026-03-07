package bg.softuni.bookshopsystem.models;

import bg.softuni.bookshopsystem.Enums.AgeRestriction;
import bg.softuni.bookshopsystem.Enums.EditionType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int book_id;

    @Enumerated(EnumType.STRING)
    private AgeRestriction age_restriction;

    @Column
    private int copies;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    private EditionType edition_type;

    @Column(precision = 19, scale = 2)
    private BigDecimal price;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column
    private String title;

    @JoinColumn(name = "author_id", nullable = false)
    @ManyToOne(optional = false)
    private Author author;

    @ManyToMany
    @JoinTable(
            name = "book_categories",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    public Book() {}

    public Book(String title, EditionType editionType, BigDecimal price, LocalDate releaseDate,
                AgeRestriction ageRestriction, Author author, Set<Category> categories, int copies) {
        this.title = title;
        this.edition_type = editionType;
        this.price = price;
        this.releaseDate = releaseDate;
        this.age_restriction = ageRestriction;
        this.author = author;
        this.categories = categories;
        this.copies = copies;
    }


    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public AgeRestriction getAge_restriction() {
        return age_restriction;
    }

    public void setAge_restriction(AgeRestriction age_restriction) {
        this.age_restriction = age_restriction;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EditionType getEdition_type() {
        return edition_type;
    }

    public void setEdition_type(EditionType edition_type) {
        this.edition_type = edition_type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getRelease_date() {
        return releaseDate;
    }

    public void setRelease_date(LocalDate release_date) {
        this.releaseDate = release_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
}
