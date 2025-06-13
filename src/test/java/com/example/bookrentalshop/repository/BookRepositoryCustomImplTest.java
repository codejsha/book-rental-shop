package com.example.bookrentalshop.repository;

import com.example.bookrentalshop.config.PersistenceConfig;
import com.example.bookrentalshop.domain.constant.BookStatus;
import com.example.bookrentalshop.domain.entity.BookEntity;
import com.example.bookrentalshop.domain.entity.CategoryEntity;
import com.example.bookrentalshop.domain.entity.QBookEntity;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({PersistenceConfig.class})
class BookRepositoryCustomImplTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepositoryCustomImpl bookRepository;

    @BeforeEach
    void setUp() {
        CategoryEntity CategoryAgile = CategoryEntity.builder().name("Agile").build();
        CategoryEntity CategorySoftwareArchitecture = CategoryEntity.builder().name("Software Architecture").build();
        CategoryEntity CategoryDataEngineering = CategoryEntity.builder().name("Data Engineering").build();
        CategoryEntity CategoryCodingPractices = CategoryEntity.builder().name("Coding Practices").build();
        CategoryEntity CategoryRefactoring = CategoryEntity.builder().name("Refactoring").build();
        CategoryEntity CategoryKubernetes = CategoryEntity.builder().name("Kubernetes").build();
        CategoryEntity CategoryMachineLearning = CategoryEntity.builder().name("Machine Learning").build();
        CategoryEntity CategoryTestDrivenDevelopment = CategoryEntity.builder().name("Test-Driven Development").build();
        CategoryEntity CategoryJava = CategoryEntity.builder().name("Java").build();
        CategoryEntity CategoryGo = CategoryEntity.builder().name("Go").build();
        CategoryEntity CategoryPython = CategoryEntity.builder().name("Python").build();

        List<CategoryEntity> categories = Lists.newArrayList(
                CategoryAgile, CategorySoftwareArchitecture, CategoryDataEngineering,
                CategoryCodingPractices, CategoryRefactoring, CategoryKubernetes,
                CategoryMachineLearning, CategoryTestDrivenDevelopment, CategoryJava,
                CategoryGo, CategoryPython
        );
        categories.forEach(entityManager::persist);

        BookEntity book1 = BookEntity.builder().author("Martin Kleppmann").title("Designing Data-Intensive Applications")
                .status(BookStatus.AVAILABLE).category(CategoryDataEngineering).build();
        BookEntity book2 = BookEntity.builder().author("Aurélien Géron").title("Hands-On Machine Learning with Scikit-Learn, Keras, and TensorFlow")
                .status(BookStatus.AVAILABLE).category(CategoryMachineLearning).build();
        BookEntity book3 = BookEntity.builder().author("Robert C. Martin").title("Clean Code")
                .status(BookStatus.AVAILABLE).category(CategoryAgile).build();
        BookEntity book4 = BookEntity.builder().author("Martin Fowler").title("Refactoring")
                .status(BookStatus.AVAILABLE).category(CategoryRefactoring).build();
        BookEntity book5 = BookEntity.builder().author("Joshua Bloch").title("Effective Java")
                .status(BookStatus.AVAILABLE).category(CategoryJava).build();
        BookEntity book6 = BookEntity.builder().author("Marko Luksa").title("Kubernetes in Action")
                .status(BookStatus.AVAILABLE).category(CategoryKubernetes).build();
        BookEntity book7 = BookEntity.builder().author("Kent Beck").title("Test-Driven Development")
                .status(BookStatus.AVAILABLE).category(CategoryTestDrivenDevelopment).build();
        BookEntity book8 = BookEntity.builder().author("Adam Freeman").title("Pro Go")
                .status(BookStatus.AVAILABLE).category(CategoryGo).build();
        BookEntity book9 = BookEntity.builder().author("Jeff Friesen").title("Learn Java Fundamentals")
                .status(BookStatus.AVAILABLE).category(CategoryJava).build();
        BookEntity book10 = BookEntity.builder().author("Laurentiu Spilca").title("Troubleshooting Java")
                .status(BookStatus.AVAILABLE).category(CategoryJava).build();
        BookEntity book11 = BookEntity.builder().author("Joshua Kerievsky").title("Refactoring to Patterns")
                .status(BookStatus.AVAILABLE).category(CategoryRefactoring).build();
        BookEntity book12 = BookEntity.builder().author("Christian Clausen").title("Five Lines of Code")
                .status(BookStatus.AVAILABLE).category(CategoryRefactoring).build();
        BookEntity book13 = BookEntity.builder().author("Luciano Ramalho").title("Fluent Python")
                .status(BookStatus.AVAILABLE).category(CategoryPython).build();
        BookEntity book14 = BookEntity.builder().author("Eric Matthes").title("Python Crash Course")
                .status(BookStatus.AVAILABLE).category(CategoryPython).build();
        BookEntity book15 = BookEntity.builder().author("Teiva Harsanyi").title("100 Go Mistakes and How to Avoid Them")
                .status(BookStatus.AVAILABLE).category(CategoryGo).build();
        BookEntity book16 = BookEntity.builder().author("Katherine Cox-Buday").title("Concurrency in Go")
                .status(BookStatus.AVAILABLE).category(CategoryGo).build();

        List<BookEntity> books = Lists.newArrayList(
                book1, book2, book3, book4, book5, book6, book7, book8, book9, book10,
                book11, book12, book13, book14, book15, book16
        );
        books.forEach(entityManager::persist);

        entityManager.flush();
    }

    @Test
    @Transactional(readOnly = true)
    void findAll_WhenNoFilter_ReturnsAllPagedBooks() {
        // arrange
        var predicate = new BooleanBuilder();
        var pageable = PageRequest.of(0, 10);

        // act
        Page<BookEntity> result = bookRepository.findAll(predicate, pageable);

        // assert
        assertEquals(0, result.getNumber());
        assertEquals(10, result.getSize());
        assertEquals(2, result.getTotalPages());
        assertEquals(16, result.getTotalElements());
    }

    @Test
    @Transactional(readOnly = true)
    void findAll_WhenFilteredByAuthor_ReturnsMatchingBooks() {
        // arrange
        var predicate = QBookEntity.bookEntity.author.containsIgnoreCase("Robert C. Martin");
        var pageable = PageRequest.of(0, 10);

        // act
        Page<BookEntity> result = bookRepository.findAll(predicate, pageable);

        // assert
        assertEquals(0, result.getNumber());
        assertEquals(10, result.getSize());
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getTotalElements());
    }
}
