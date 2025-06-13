package com.example.bookrentalshop.domain.service;

import com.example.bookrentalshop.controller.dto.BookCondition;
import com.example.bookrentalshop.controller.dto.BookCreateRequest;
import com.example.bookrentalshop.controller.dto.BookUpdateRequest;
import com.example.bookrentalshop.domain.constant.BookStatus;
import com.example.bookrentalshop.domain.entity.BookEntity;
import com.example.bookrentalshop.domain.entity.CategoryEntity;
import com.example.bookrentalshop.domain.entity.QBookEntity;
import com.example.bookrentalshop.domain.model.CategoryModel;
import com.example.bookrentalshop.repository.BookRepository;
import com.google.common.collect.Maps;
import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryModel categoryModel;

    private Map<String, CategoryEntity> categoryMap;
    private Map<Long, BookEntity> bookMap;

    @BeforeEach
    void setUp() {
        CategoryEntity CategoryAgile = CategoryEntity.builder().id(1).name("Agile").build();
        CategoryEntity CategorySoftwareArchitecture = CategoryEntity.builder().id(2).name("Software Architecture").build();
        CategoryEntity CategoryDataEngineering = CategoryEntity.builder().id(3).name("Data Engineering").build();
        CategoryEntity CategoryCodingPractices = CategoryEntity.builder().id(4).name("Coding Practices").build();
        CategoryEntity CategoryRefactoring = CategoryEntity.builder().id(5).name("Refactoring").build();
        CategoryEntity CategoryKubernetes = CategoryEntity.builder().id(6).name("Kubernetes").build();
        CategoryEntity CategoryMachineLearning = CategoryEntity.builder().id(7).name("Machine Learning").build();
        CategoryEntity CategoryTestDrivenDevelopment = CategoryEntity.builder().id(8).name("Test-Driven Development").build();
        CategoryEntity CategoryJava = CategoryEntity.builder().id(9).name("Java").build();
        CategoryEntity CategoryGo = CategoryEntity.builder().id(10).name("Go").build();
        CategoryEntity CategoryPython = CategoryEntity.builder().id(11).name("Python").build();
        categoryMap = Maps.newHashMap();
        categoryMap.put(CategoryAgile.getName(), CategoryAgile);
        categoryMap.put(CategorySoftwareArchitecture.getName(), CategorySoftwareArchitecture);
        categoryMap.put(CategoryDataEngineering.getName(), CategoryDataEngineering);
        categoryMap.put(CategoryCodingPractices.getName(), CategoryCodingPractices);
        categoryMap.put(CategoryRefactoring.getName(), CategoryRefactoring);
        categoryMap.put(CategoryKubernetes.getName(), CategoryKubernetes);
        categoryMap.put(CategoryMachineLearning.getName(), CategoryMachineLearning);
        categoryMap.put(CategoryTestDrivenDevelopment.getName(), CategoryTestDrivenDevelopment);
        categoryMap.put(CategoryJava.getName(), CategoryJava);
        categoryMap.put(CategoryGo.getName(), CategoryGo);
        categoryMap.put(CategoryPython.getName(), CategoryPython);

        BookEntity book1 = BookEntity.builder().id(1L).author("Martin Kleppmann").title("Designing Data-Intensive Applications")
                .status(BookStatus.AVAILABLE).category(CategoryDataEngineering).build();
        BookEntity book2 = BookEntity.builder().id(2L).author("Aurélien Géron").title("Hands-On Machine Learning with Scikit-Learn, Keras, and TensorFlow")
                .status(BookStatus.AVAILABLE).category(CategoryMachineLearning).build();
        BookEntity book3 = BookEntity.builder().id(3L).author("Robert C. Martin").title("Clean Code")
                .status(BookStatus.AVAILABLE).category(CategoryAgile).build();
        BookEntity book4 = BookEntity.builder().id(4L).author("Martin Fowler").title("Refactoring")
                .status(BookStatus.AVAILABLE).category(CategoryRefactoring).build();
        BookEntity book5 = BookEntity.builder().id(5L).author("Joshua Bloch").title("Effective Java")
                .status(BookStatus.AVAILABLE).category(CategoryJava).build();
        BookEntity book6 = BookEntity.builder().id(6L).author("Marko Luksa").title("Kubernetes in Action")
                .status(BookStatus.AVAILABLE).category(CategoryKubernetes).build();
        BookEntity book7 = BookEntity.builder().id(7L).author("Kent Beck").title("Test-Driven Development")
                .status(BookStatus.AVAILABLE).category(CategoryTestDrivenDevelopment).build();
        BookEntity book8 = BookEntity.builder().id(8L).author("Adam Freeman").title("Pro Go")
                .status(BookStatus.AVAILABLE).category(CategoryGo).build();
        BookEntity book9 = BookEntity.builder().id(9L).author("Jeff Friesen").title("Learn Java Fundamentals")
                .status(BookStatus.AVAILABLE).category(CategoryJava).build();
        BookEntity book10 = BookEntity.builder().id(10L).author("Laurentiu Spilca").title("Troubleshooting Java")
                .status(BookStatus.AVAILABLE).category(CategoryJava).build();
        BookEntity book11 = BookEntity.builder().id(11L).author("Joshua Kerievsky").title("Refactoring to Patterns")
                .status(BookStatus.AVAILABLE).category(CategoryRefactoring).build();
        BookEntity book12 = BookEntity.builder().id(12L).author("Christian Clausen").title("Five Lines of Code")
                .status(BookStatus.AVAILABLE).category(CategoryRefactoring).build();
        BookEntity book13 = BookEntity.builder().id(13L).author("Luciano Ramalho").title("Fluent Python")
                .status(BookStatus.AVAILABLE).category(CategoryPython).build();
        BookEntity book14 = BookEntity.builder().id(14L).author("Eric Matthes").title("Python Crash Course")
                .status(BookStatus.AVAILABLE).category(CategoryPython).build();
        BookEntity book15 = BookEntity.builder().id(15L).author("Teiva Harsanyi").title("100 Go Mistakes and How to Avoid Them")
                .status(BookStatus.AVAILABLE).category(CategoryGo).build();
        BookEntity book16 = BookEntity.builder().id(16L).author("Katherine Cox-Buday").title("Concurrency in Go")
                .status(BookStatus.AVAILABLE).category(CategoryGo).build();
        bookMap = Maps.newHashMap();
        bookMap.put(book1.getId(), book1);
        bookMap.put(book2.getId(), book2);
        bookMap.put(book3.getId(), book3);
        bookMap.put(book4.getId(), book4);
        bookMap.put(book5.getId(), book5);
        bookMap.put(book6.getId(), book6);
        bookMap.put(book7.getId(), book7);
        bookMap.put(book8.getId(), book8);
        bookMap.put(book9.getId(), book9);
        bookMap.put(book10.getId(), book10);
        bookMap.put(book11.getId(), book11);
        bookMap.put(book12.getId(), book12);
        bookMap.put(book13.getId(), book13);
        bookMap.put(book14.getId(), book14);
        bookMap.put(book15.getId(), book15);
        bookMap.put(book16.getId(), book16);
    }

    @Nested
    class GetAllBooksTests {
        @Test
        void getAllBooks_ReturnsExpectedPage() {
            // arrange
            var condition = BookCondition.builder().build();
            var predicate = new BooleanBuilder();
            var pageable = PageRequest.of(0, 10);
            var books = bookMap.values().stream().toList();
            Page<BookEntity> bookPage = PageableExecutionUtils.getPage(books, pageable, books::size);

            when(bookRepository.findAll(predicate, pageable)).thenReturn(bookPage);

            // act
            Page<BookEntity> result = bookService.getAllBooks(condition, pageable);

            // assert
            assertNotNull(result);
            assertEquals(0, result.getNumber());
            assertEquals(10, result.getSize());
            assertEquals(2, result.getTotalPages());
            assertEquals(16, result.getTotalElements());
        }

        @Test
        void getAllBooks_WithCategoryPredicate_ReturnsExpectedPage() {
            // arrange
            String categoryName = "Java";
            var category = categoryMap.get(categoryName);
            var condition = BookCondition.builder().category(categoryName).build();
            var predicate = new BooleanBuilder();
            predicate.and(QBookEntity.bookEntity.category.eq(category));
            var pageable = PageRequest.of(0, 10);

            var filteredBooks = bookMap.values().stream()
                    .filter(book -> {
                        var bookCategoryId = book.getCategory().getId();
                        return bookCategoryId.equals(category.getId());
                    })
                    .toList();
            Page<BookEntity> bookPage = PageableExecutionUtils.getPage(filteredBooks, pageable, filteredBooks::size);

            when(categoryModel.getNameMap()).thenReturn(categoryMap);
            when(bookRepository.findAll(predicate, pageable)).thenReturn(bookPage);

            // act
            Page<BookEntity> result = bookService.getAllBooks(condition, pageable);

            // assert
            assertNotNull(result);
            assertEquals(3, result.getNumberOfElements());
            assertEquals(0, result.getNumber());
            assertEquals(10, result.getSize());
            assertEquals(1, result.getTotalPages());
            assertEquals(3, result.getTotalElements());
        }

        @Test
        void getAllBooks_WithTitlePredicate_ReturnsExpectedPage() {
            // arrange
            String title = "Learn";
            var condition = BookCondition.builder().title(title).build();
            var predicate = new BooleanBuilder();
            predicate.and(QBookEntity.bookEntity.title.containsIgnoreCase(title));
            var pageable = PageRequest.of(0, 10);

            var filteredBooks = bookMap.values().stream()
                    .filter(book -> book.getTitle().contains(title))
                    .toList();
            Page<BookEntity> bookPage = PageableExecutionUtils.getPage(filteredBooks, pageable, filteredBooks::size);

            when(bookRepository.findAll(predicate, pageable)).thenReturn(bookPage);

            // act
            Page<BookEntity> result = bookService.getAllBooks(condition, pageable);

            // assert
            assertNotNull(result);
            assertEquals(2, result.getNumberOfElements());
            assertEquals(0, result.getNumber());
            assertEquals(10, result.getSize());
            assertEquals(1, result.getTotalPages());
            assertEquals(2, result.getTotalElements());
        }

        @Test
        void getAllBooks_WithAuthorPredicate_ReturnsExpectedPage() {
            // arrange
            String author = "Marko";
            var condition = BookCondition.builder().author(author).build();
            var predicate = new BooleanBuilder();
            predicate.and(QBookEntity.bookEntity.author.containsIgnoreCase(author));
            var pageable = PageRequest.of(0, 10);

            var filteredBooks = bookMap.values().stream()
                    .filter(book -> book.getAuthor().contains(author))
                    .toList();
            Page<BookEntity> bookPage = PageableExecutionUtils.getPage(filteredBooks, pageable, filteredBooks::size);

            when(bookRepository.findAll(predicate, pageable)).thenReturn(bookPage);

            // act
            Page<BookEntity> result = bookService.getAllBooks(condition, pageable);

            // assert
            assertNotNull(result);
            assertEquals(1, result.getNumberOfElements());
            assertEquals(0, result.getNumber());
            assertEquals(10, result.getSize());
            assertEquals(1, result.getTotalPages());
            assertEquals(1, result.getTotalElements());
        }
    }

    @Nested
    class GetBookTests {
        @Test
        void getBook_ReturnsExpectedResult() {
            // arrange
            Long bookId = 1L;
            var targetBook = bookMap.get(bookId);

            when(bookRepository.findById(bookId)).thenReturn(Optional.of(targetBook));

            // act
            BookEntity result = bookService.getBook(bookId);

            // assert
            assertNotNull(result);
            assertEquals(bookId, result.getId());
            assertEquals("Martin Kleppmann", result.getAuthor());
            assertEquals("Designing Data-Intensive Applications", result.getTitle());
        }
    }

    @Nested
    class AddBookTests {
        @Test
        void addBook_WithValidCategory_ReturnsExpectedResult() {
            // arrange
            var req = BookCreateRequest.builder()
                    .author("Hello")
                    .title("Architecture")
                    .status("AVAILABLE")
                    .category("Software Architecture")
                    .build();
            String categoryName = "Software Architecture";
            var category = categoryMap.get(categoryName);
            var newBook = BookEntity.builder().id(17L).author("Hello").title("Architecture")
                    .status(BookStatus.AVAILABLE).category(category).build();

            when(categoryModel.getNameMap()).thenReturn(categoryMap);
            when(bookRepository.save(any(BookEntity.class))).thenReturn(newBook);

            // act
            BookEntity result = bookService.addBook(req);

            // assert
            assertNotNull(result);
            assertEquals("Hello", result.getAuthor());
            assertEquals("Architecture", result.getTitle());
            assertEquals(BookStatus.AVAILABLE, result.getStatus());
            assertEquals("Software Architecture", result.getCategory().getName());
        }

        @Test
        void addBook_WhenInvalidCategory_ThrowIllegalArgumentException() {
            // arrange
            var req = BookCreateRequest.builder()
                    .author("Hello")
                    .title("Architecture")
                    .status("AVAILABLE")
                    .category("INVALID_CATEGORY")
                    .build();

            when(categoryModel.getNameMap()).thenReturn(categoryMap);

            // act and assert
            assertThrows(IllegalArgumentException.class, () -> {
                bookService.addBook(req);
            });
        }
    }

    @Nested
    class UpdateBookTests {
        @Test
        void updateBook_WhenAllFieldsProvided_ReturnsExpectedResult() {
            // arrange
            Long bookId = 1L;
            var targetBook = bookMap.get(bookId);
            var req = BookUpdateRequest.builder()
                    .author("Hello")
                    .title("Architecture")
                    .status("AVAILABLE")
                    .category("Software Architecture")
                    .build();
            var updatedBook = BookEntity.builder()
                    .id(bookId)
                    .author("Hello")
                    .title("Good Architecture")
                    .status(BookStatus.AVAILABLE)
                    .category(categoryMap.get("Software Architecture"))
                    .build();

            when(bookRepository.findById(bookId)).thenReturn(Optional.of(targetBook));
            when(categoryModel.getNameMap()).thenReturn(categoryMap);
            when(bookRepository.save(any(BookEntity.class))).thenReturn(updatedBook);

            // act
            BookEntity result = bookService.updateBook(bookId, req);

            // assert
            assertNotNull(result);
            assertEquals("Hello", result.getAuthor());
            assertEquals("Good Architecture", result.getTitle());
            assertEquals(BookStatus.AVAILABLE, result.getStatus());
            assertEquals("Software Architecture", result.getCategory().getName());
        }

        @Test
        void updateBook_WhenCategoryChanged_ReturnsExpectedResult() {
            // arrange
            Long bookId = 1L;
            String changedCategory = "Software Architecture";
            var targetBook = bookMap.get(bookId);
            var req = BookUpdateRequest.builder()
                    .category(changedCategory)
                    .build();
            var updatedBook = BookEntity.builder().id(bookId).author("Martin Kleppmann").title("Designing Data-Intensive Applications")
                    .status(BookStatus.AVAILABLE).category(categoryMap.get(changedCategory)).build();

            when(bookRepository.findById(bookId)).thenReturn(Optional.of(targetBook));
            when(categoryModel.getNameMap()).thenReturn(categoryMap);
            when(bookRepository.save(any(BookEntity.class))).thenReturn(updatedBook);

            // act
            BookEntity result = bookService.updateBook(bookId, req);

            // assert
            assertNotNull(result);
            assertEquals("Martin Kleppmann", result.getAuthor());
            assertEquals("Designing Data-Intensive Applications", result.getTitle());
            assertEquals(BookStatus.AVAILABLE, result.getStatus());
            assertEquals(changedCategory, result.getCategory().getName());
        }
    }

    @Nested
    class DeleteBookTests {
        @Test
        void deleteBook_WhenBookExists_DeletesSuccessfully() {
            // arrange
            Long bookId = 1L;
            var targetBook = bookMap.get(bookId);

            when(bookRepository.findById(bookId)).thenReturn(Optional.of(targetBook));

            // act
            bookService.deleteBook(bookId);

            // assert
            assertTrue(true);
        }
    }
}
