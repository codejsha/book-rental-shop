package com.example.bookrentalshop.controller;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.example.bookrentalshop.controller.dto.RentalCheckOutRequest;
import com.example.bookrentalshop.controller.dto.RentalCondition;
import com.example.bookrentalshop.domain.constant.BookStatus;
import com.example.bookrentalshop.domain.entity.BookEntity;
import com.example.bookrentalshop.domain.entity.CategoryEntity;
import com.example.bookrentalshop.domain.entity.RentalEntity;
import com.example.bookrentalshop.domain.entity.UserEntity;
import com.example.bookrentalshop.domain.service.RentalService;
import com.example.bookrentalshop.support.restdocs.RestDocsControllerSupport;
import com.example.bookrentalshop.support.security.WithMockPrincipal;
import com.example.bookrentalshop.support.security.filter.JwtAuthenticationFilter;
import com.google.common.collect.Lists;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RentalController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
class RentalControllerTest extends RestDocsControllerSupport {

    @MockitoBean
    private RentalService rentalService;

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
    void getAllRentals() throws Exception {
        // arrange
        var pageable = PageRequest.of(0, 10);
        var user = UserEntity.builder()
                .email("user@example.com")
                .password("password")
                .name("User")
                .build();
        var rental1 = RentalEntity.builder()
                .id(1L)
                .book(bookMap.get(4L))
                .user(user)
                .checkOutDate(LocalDateTime.now())
                .returnDate(null)
                .build();
        var rental2 = RentalEntity.builder()
                .id(2L)
                .book(bookMap.get(5L))
                .user(user)
                .checkOutDate(LocalDateTime.now())
                .returnDate(null)
                .build();

        List<RentalEntity> rentals = Lists.newArrayList(rental1, rental2);
        Page<RentalEntity> rentalPage = PageableExecutionUtils.getPage(rentals, pageable, rentals::size);

        when(rentalService.getAllRentals(any(RentalCondition.class), any(Pageable.class))).thenReturn(rentalPage);

        // act
        var execute = mockMvc.perform(get("/api/v1/rentals")
                .accept(MediaType.APPLICATION_JSON));

        // assert
        execute.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(RentalController.class))
                .andExpect(handler().methodName("getAllRentals"));

        // document
        execute.andDo(MockMvcRestDocumentationWrapper.document(OPENAPI_DOCUMENT_IDENTIFIER,
                ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                        .tag("Rental")
                        .summary("Find all rentals")
                        .description("Find all rental records with optional filters.")
                        .responseSchema(Schema.schema("Page<RentalGetResponse>"))
                        .queryParameters(
                                parameterWithName("bookId").description("Book ID").optional(),
                                parameterWithName("userId").description("User ID").optional(),
                                parameterWithName("page").description("Page number").defaultValue("0").optional(),
                                parameterWithName("size").description("Page size").defaultValue("10").optional(),
                                parameterWithName("sort").description("Sorting").defaultValue("id,desc").optional())
                        .build()
                )));
    }

    @Test
    @WithMockPrincipal
    void checkOutRental() throws Exception {
        // arrange
        Long userId = 1L;
        Long bookId = 1L;
        var user = UserEntity.builder()
                .id(userId)
                .email("user@example.com")
                .password("password")
                .name("User")
                .build();
        var rental = RentalEntity.builder()
                .id(userId)
                .book(bookMap.get(bookId))
                .user(user)
                .checkOutDate(LocalDateTime.now())
                .returnDate(null)
                .build();

        var req = RentalCheckOutRequest.builder()
                .bookId(bookId)
                .build();

        when(rentalService.createCheckOut(any(RentalCheckOutRequest.class), any(Principal.class))).thenReturn(rental);

        // act
        var execute = mockMvc.perform(post("/api/v1/rentals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
                .accept(MediaType.APPLICATION_JSON));

        // assert
        execute.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(RentalController.class))
                .andExpect(handler().methodName("checkOutRental"));

        // document
        execute.andDo(MockMvcRestDocumentationWrapper.document(OPENAPI_DOCUMENT_IDENTIFIER,
                ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                        .tag("Rental")
                        .summary("Rent a book")
                        .description("Rents a book for a user.")
                        .requestSchema(Schema.schema("RentalCheckOutRequest"))
                        .requestFields(
                                fieldWithPath("bookId").description("Book ID"))
                        .responseSchema(Schema.schema("RentalCheckOutResponse"))
                        .responseFields(
                                fieldWithPath("bookId").description("Book ID"),
                                fieldWithPath("userId").description("User ID"),
                                fieldWithPath("checkOutDate").description("Check-out date"))
                        .build()
                )));
    }

    @Test
    void getRental() throws Exception {
        // arrange
        Long rentalId = 1L;
        Long userId = 1L;
        Long bookId = 1L;
        var user = UserEntity.builder()
                .id(userId)
                .email("user@example.com")
                .password("password")
                .name("User")
                .build();
        var rental = RentalEntity.builder()
                .id(userId)
                .book(bookMap.get(bookId))
                .user(user)
                .checkOutDate(LocalDateTime.now())
                .returnDate(null)
                .build();

        when(rentalService.getRental(any(Long.class))).thenReturn(rental);

        // act
        var execute = mockMvc.perform(get("/api/v1/rentals/{id}", rentalId)
                .accept(MediaType.APPLICATION_JSON));

        // assert
        execute.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(RentalController.class))
                .andExpect(handler().methodName("getRental"));

        // document
        execute.andDo(MockMvcRestDocumentationWrapper.document(OPENAPI_DOCUMENT_IDENTIFIER,
                ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                        .tag("Rental")
                        .summary("Find rental by ID")
                        .description("Finds a rental record by its ID.")
                        .pathParameters(
                                parameterWithName("id").description("Rental ID"))
                        .responseSchema(Schema.schema("RentalGetResponse"))
                        .responseFields(
                                fieldWithPath("id").description("Rental ID"),
                                fieldWithPath("bookId").description("Book ID"),
                                fieldWithPath("bookTitle").description("Book title"),
                                fieldWithPath("userId").description("User ID"),
                                fieldWithPath("userName").description("User name"),
                                fieldWithPath("checkOutDate").description("Check-out date"),
                                fieldWithPath("returnDate").description("Return date"))
                        .build()
                )));
    }

    @Test
    @WithMockPrincipal
    void returnRental() throws Exception {
        // arrange
        Long bookId = 6L;
        var targetBook = bookMap.get(bookId);
        var user = UserEntity.builder()
                .id(1L)
                .email("user@example.com")
                .password("password")
                .name("User")
                .build();
        var rental = RentalEntity.builder()
                .id(1L)
                .book(targetBook)
                .user(user)
                .checkOutDate(LocalDateTime.now())
                .returnDate(LocalDateTime.now())
                .build();

        when(rentalService.createReturn(any(Long.class), any(Principal.class))).thenReturn(rental);

        // act
        var execute = mockMvc.perform(put("/api/v1/rentals/{id}", bookId)
                .accept(MediaType.APPLICATION_JSON));

        // assert
        execute.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(RentalController.class))
                .andExpect(handler().methodName("returnRental"));

        // document
        execute.andDo(MockMvcRestDocumentationWrapper.document(OPENAPI_DOCUMENT_IDENTIFIER,
                ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                        .tag("Rental")
                        .summary("Return a book")
                        .description("Returns a rented book.")
                        .pathParameters(
                                parameterWithName("id").description("Rental ID")
                        )
                        .responseSchema(Schema.schema("RentalReturnResponse"))
                        .responseFields(
                                fieldWithPath("bookId").description("Book ID"),
                                fieldWithPath("userId").description("User ID"),
                                fieldWithPath("checkOutDate").description("Check-out date"),
                                fieldWithPath("returnDate").description("Return date"))
                        .build()
                )));
    }

    @Test
    @WithMockPrincipal
    void getMyRentals() throws Exception {
        // arrange
        var pageable = PageRequest.of(0, 10);
        var user = UserEntity.builder()
                .email("user@example.com")
                .password("password")
                .name("User")
                .build();
        var rental1 = RentalEntity.builder()
                .id(1L)
                .book(bookMap.get(4L))
                .user(user)
                .checkOutDate(LocalDateTime.now())
                .returnDate(null)
                .build();
        var rental2 = RentalEntity.builder()
                .id(2L)
                .book(bookMap.get(5L))
                .user(user)
                .checkOutDate(LocalDateTime.now())
                .returnDate(null)
                .build();

        List<RentalEntity> rentals = Lists.newArrayList(rental1, rental2);
        Page<RentalEntity> rentalPage = PageableExecutionUtils.getPage(rentals, pageable, rentals::size);

        when(rentalService.getAllRentals(any(RentalCondition.class), any(Pageable.class), any(Principal.class)))
                .thenReturn(rentalPage);

        // act
        var execute = mockMvc.perform(get("/api/v1/rentals/my-rentals")
                .accept(MediaType.APPLICATION_JSON));

        // assert
        execute.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(RentalController.class))
                .andExpect(handler().methodName("getMyRentals"));

        // document
        execute.andDo(MockMvcRestDocumentationWrapper.document(OPENAPI_DOCUMENT_IDENTIFIER,
                ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                        .tag("Rental")
                        .summary("Find my rentals")
                        .description("Finds all rental records for the authenticated user.")
                        .responseSchema(Schema.schema("Page<RentalGetResponse>"))
                        .queryParameters(
                                parameterWithName("bookId").description("Book ID").optional(),
                                parameterWithName("userId").description("User ID").optional(),
                                parameterWithName("page").description("Page number").defaultValue("0").optional(),
                                parameterWithName("size").description("Page size").defaultValue("10").optional(),
                                parameterWithName("sort").description("Sorting").defaultValue("id,desc").optional())
                        .build()
                )));
    }

    @Test
    @WithMockPrincipal
    void getMyRental() throws Exception {
        // arrange
        var user = UserEntity.builder()
                .email("user@example.com")
                .password("password")
                .name("User")
                .build();
        var rental = RentalEntity.builder()
                .id(1L)
                .book(bookMap.get(4L))
                .user(user)
                .checkOutDate(LocalDateTime.now())
                .returnDate(null)
                .build();

        when(rentalService.getRental(any(Long.class), any(Principal.class))).thenReturn(rental);

        // act
        var execute = mockMvc.perform(get("/api/v1/rentals/my-rentals/{id}", rental.getId())
                .accept(MediaType.APPLICATION_JSON));

        // assert
        execute.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(RentalController.class))
                .andExpect(handler().methodName("getMyRental"));

        // document
        execute.andDo(MockMvcRestDocumentationWrapper.document(OPENAPI_DOCUMENT_IDENTIFIER,
                ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                        .tag("Rental")
                        .summary("Find my rental by ID")
                        .description("Finds a rental record by its ID.")
                        .pathParameters(
                                parameterWithName("id").description("Rental ID"))
                        .responseSchema(Schema.schema("RentalGetResponse"))
                        .responseFields(
                                fieldWithPath("id").description("Rental ID"),
                                fieldWithPath("bookId").description("Book ID"),
                                fieldWithPath("bookTitle").description("Book title"),
                                fieldWithPath("userId").description("User ID"),
                                fieldWithPath("userName").description("User name"),
                                fieldWithPath("checkOutDate").description("Check-out date"),
                                fieldWithPath("returnDate").description("Return date"))
                        .build()
                )));
    }
}
