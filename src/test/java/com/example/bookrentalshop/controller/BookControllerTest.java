package com.example.bookrentalshop.controller;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.example.bookrentalshop.controller.dto.BookCondition;
import com.example.bookrentalshop.controller.dto.BookCreateRequest;
import com.example.bookrentalshop.controller.dto.BookUpdateRequest;
import com.example.bookrentalshop.domain.constant.BookStatus;
import com.example.bookrentalshop.domain.entity.BookEntity;
import com.example.bookrentalshop.domain.entity.CategoryEntity;
import com.example.bookrentalshop.domain.service.BookService;
import com.example.bookrentalshop.support.restdocs.RestDocsControllerSupport;
import com.example.bookrentalshop.support.security.filter.JwtAuthenticationFilter;
import com.google.common.collect.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Map;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
class BookControllerTest extends RestDocsControllerSupport {

    @MockitoBean
    private BookService bookService;

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

    @Test
    void getAllBooks() throws Exception {
        // arrange
        var pageable = PageRequest.of(0, 10);
        var books = bookMap.values().stream().toList();
        Page<BookEntity> bookPage = PageableExecutionUtils.getPage(books, pageable, books::size);

        when(bookService.getAllBooks(any(BookCondition.class), any(Pageable.class))).thenReturn(bookPage);

        // act
        var execute = mockMvc.perform(get("/api/v1/books")
                .accept(MediaType.APPLICATION_JSON));

        // assert
        execute.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(BookController.class))
                .andExpect(handler().methodName("getAllBooks"));

        // document
        execute.andDo(MockMvcRestDocumentationWrapper.document(OPENAPI_DOCUMENT_IDENTIFIER,
                ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                        .tag("Book")
                        .summary("Find All Books")
                        .description("Finds all books with optional filters.")
                        .responseSchema(Schema.schema("Page<BookGetResponse>"))
                        .queryParameters(
                                parameterWithName("author").description("Book author").optional(),
                                parameterWithName("title").description("Book title").optional(),
                                parameterWithName("status").description("Book status").optional(),
                                parameterWithName("category").description("Book category").optional(),
                                parameterWithName("page").description("Page number").defaultValue("0").optional(),
                                parameterWithName("size").description("Page size").defaultValue("10").optional(),
                                parameterWithName("sort").description("Sorting").defaultValue("id,desc").optional())
                        .build()
                )));
    }

    @Test
    void addBook() throws Exception {
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

        when(bookService.addBook(any(BookCreateRequest.class))).thenReturn(newBook);

        // act
        var execute = mockMvc.perform(post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
                .accept(MediaType.APPLICATION_JSON));

        // assert
        execute.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(handler().handlerType(BookController.class))
                .andExpect(handler().methodName("addBook"))
                .andExpect(header().string("Location", "/api/v1/books/" + newBook.getId()));

        // document
        execute.andDo(MockMvcRestDocumentationWrapper.document(OPENAPI_DOCUMENT_IDENTIFIER,
                ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                        .tag("Book")
                        .summary("Register Book")
                        .description("Registers a new book.")
                        .requestSchema(Schema.schema("BookCreateRequest"))
                        .requestFields(
                                fieldWithPath("author").description("Book author"),
                                fieldWithPath("title").description("Book title"),
                                fieldWithPath("status").description("Book status"),
                                fieldWithPath("category").description("Book category"))
                        .responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location of the created book"))
                        .build()
                )));
    }

    @Test
    void getBook() throws Exception {
        // arrange
        Long bookId = 1L;
        var targetBook = bookMap.get(bookId);

        when(bookService.getBook(any(Long.class))).thenReturn(targetBook);

        // act
        var execute = mockMvc.perform(get("/api/v1/books/{id}", bookId)
                .accept(MediaType.APPLICATION_JSON));

        // assert
        execute.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(BookController.class))
                .andExpect(handler().methodName("getBook"))
                .andExpect(jsonPath("$.id").value(targetBook.getId()))
                .andExpect(jsonPath("$.author").value(targetBook.getAuthor()))
                .andExpect(jsonPath("$.title").value(targetBook.getTitle()))
                .andExpect(jsonPath("$.status").value(targetBook.getStatus().toString()))
                .andExpect(jsonPath("$.category").value(targetBook.getCategory().getName()));

        // document
        execute.andDo(MockMvcRestDocumentationWrapper.document(OPENAPI_DOCUMENT_IDENTIFIER,
                ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                        .tag("Book")
                        .summary("Find a Book")
                        .description("Finds a book by its ID.")
                        .pathParameters(
                                parameterWithName("id").description("Book ID"))
                        .responseSchema(Schema.schema("BookGetResponse"))
                        .responseFields(
                                fieldWithPath("id").description("Book ID"),
                                fieldWithPath("author").description("Book author"),
                                fieldWithPath("title").description("Book title"),
                                fieldWithPath("status").description("Book status"),
                                fieldWithPath("category").description("Book category"))
                        .build()
                )));
    }

    @Test
    void updateBook() throws Exception {
        // arrange
        Long bookId = 1L;
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

        when(bookService.updateBook(any(Long.class), any(BookUpdateRequest.class))).thenReturn(updatedBook);

        // act
        var execute = mockMvc.perform(put("/api/v1/books/{id}", bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
                .accept(MediaType.APPLICATION_JSON));

        // assert
        execute.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(BookController.class))
                .andExpect(handler().methodName("updateBook"))
                .andExpect(jsonPath("$.id").value(updatedBook.getId()))
                .andExpect(jsonPath("$.author").value(updatedBook.getAuthor()))
                .andExpect(jsonPath("$.title").value(updatedBook.getTitle()))
                .andExpect(jsonPath("$.status").value(updatedBook.getStatus().toString()))
                .andExpect(jsonPath("$.category").value(updatedBook.getCategory().getName()));

        // document
        execute.andDo(MockMvcRestDocumentationWrapper.document(OPENAPI_DOCUMENT_IDENTIFIER,
                ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                        .tag("Book")
                        .summary("Update Book")
                        .description("Updates an existing book by its ID.")
                        .pathParameters(
                                parameterWithName("id").description("Book ID"))
                        .requestSchema(Schema.schema("BookUpdateRequest"))
                        .requestFields(
                                fieldWithPath("author").description("Book author").optional(),
                                fieldWithPath("title").description("Book title").optional(),
                                fieldWithPath("status").description("Book status").optional(),
                                fieldWithPath("category").description("Book category").optional())
                        .responseSchema(Schema.schema("BookUpdateResponse"))
                        .responseFields(
                                fieldWithPath("id").description("Book ID"),
                                fieldWithPath("author").description("Book author"),
                                fieldWithPath("title").description("Book title"),
                                fieldWithPath("status").description("Book status"),
                                fieldWithPath("category").description("Book category"))
                        .build()
                )));
    }

    @Test
    void deleteBook() throws Exception {
        // arrange
        Long bookId = 1L;

        // act
        var execute = mockMvc.perform(delete("/api/v1/books/{id}", bookId)
                .accept(MediaType.APPLICATION_JSON));

        // assert
        execute.andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(handler().handlerType(BookController.class))
                .andExpect(handler().methodName("deleteBook"));

        // document
        execute.andDo(MockMvcRestDocumentationWrapper.document(OPENAPI_DOCUMENT_IDENTIFIER,
                ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                        .tag("Book")
                        .summary("Delete Book")
                        .description("Deletes a book by its ID.")
                        .pathParameters(
                                parameterWithName("id").description("Book ID"))
                        .build()
                )));
    }
}
