package com.example.bookrentalshop.domain.service;

import com.example.bookrentalshop.controller.dto.RentalCheckOutRequest;
import com.example.bookrentalshop.controller.dto.RentalCondition;
import com.example.bookrentalshop.domain.constant.BookStatus;
import com.example.bookrentalshop.domain.constant.UserAuthority;
import com.example.bookrentalshop.domain.entity.BookEntity;
import com.example.bookrentalshop.domain.entity.CategoryEntity;
import com.example.bookrentalshop.domain.entity.RentalEntity;
import com.example.bookrentalshop.domain.entity.UserEntity;
import com.example.bookrentalshop.repository.BookRepository;
import com.example.bookrentalshop.repository.RentalRepository;
import com.example.bookrentalshop.repository.UserRepository;
import com.example.bookrentalshop.support.exception.rental.RentalNotAvailableException;
import com.example.bookrentalshop.support.exception.rental.RentalReturnNotAllowedException;
import com.example.bookrentalshop.support.security.JwtAuthenticationPrincipal;
import com.example.bookrentalshop.support.security.JwtAuthenticationToken;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @InjectMocks
    private RentalService rentalService;

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

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
                .status(BookStatus.CHECKED_OUT).category(CategoryKubernetes).build();
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
                .status(BookStatus.CHECKED_OUT).category(CategoryGo).build();
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
    class GetAllRentalsTests {
        @Test
        void getAllRentals_ReturnsExpectedPage() {
            // arrange
            var condition = RentalCondition.builder().build();
            var predicate = new BooleanBuilder();
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

            when(rentalRepository.findAll(predicate, pageable)).thenReturn(rentalPage);

            // act
            Page<RentalEntity> result = rentalService.getAllRentals(condition, pageable);

            // assert
            assertNotNull(result);
            assertEquals(2, result.getNumberOfElements());
            assertEquals(0, result.getNumber());
            assertEquals(10, result.getSize());
            assertEquals(1, result.getTotalPages());
            assertEquals(2, result.getTotalElements());

            assertThat(result.stream().map(RentalEntity::getId).toList()).containsExactlyInAnyOrder(1L, 2L);
            assertThat(result.stream().map(RentalEntity::getBook).map(BookEntity::getId).toList()).containsExactlyInAnyOrder(4L, 5L);
        }

        @Test
        void getAllRentals_WithUserDetails_ReturnsExpectedPage() {
            // arrange
            var condition = RentalCondition.builder().build();
            var pageable = PageRequest.of(0, 10);

            var user1 = UserEntity.builder()
                    .id(1L)
                    .email("user1@example.com")
                    .password("password")
                    .name("User1")
                    .build();
            var user2 = UserEntity.builder()
                    .id(2L)
                    .email("user2@example.com")
                    .password("password")
                    .name("User2")
                    .build();

            var rental1 = RentalEntity.builder()
                    .id(1L)
                    .book(bookMap.get(1L))
                    .user(user1)
                    .checkOutDate(LocalDateTime.now())
                    .returnDate(null)
                    .build();
            var rental2 = RentalEntity.builder()
                    .id(2L)
                    .book(bookMap.get(2L))
                    .user(user2)
                    .checkOutDate(LocalDateTime.now())
                    .returnDate(null)
                    .build();
            var rental3 = RentalEntity.builder()
                    .id(3L)
                    .book(bookMap.get(3L))
                    .user(user2)
                    .checkOutDate(LocalDateTime.now())
                    .returnDate(null)
                    .build();

            var accessToken = "access-token";
            var principal1 = new JwtAuthenticationPrincipal(user1.getId(), user1.getEmail());
            var authPrincipal1 = new JwtAuthenticationToken(principal1, accessToken, UserAuthority.USER_DEFAULT_AUTHORITIES);

            List<RentalEntity> rentals = Lists.newArrayList(rental1, rental2, rental3);
            List<RentalEntity> filteredRentals = rentals.stream()
                    .filter(rental -> rental.getUser().getId().equals(user1.getId()))
                    .toList();
            Page<RentalEntity> rentalPage = PageableExecutionUtils.getPage(filteredRentals, pageable, filteredRentals::size);

            when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));
            when(rentalRepository.findAll(any(Predicate.class), eq(pageable))).thenReturn(rentalPage);

            // act
            Page<RentalEntity> result = rentalService.getAllRentals(condition, pageable, authPrincipal1);

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
    class GetRentalTests {
        @Test
        void getRental_ReturnsExpectedResult() {
            // arrange
            Long rentalId = 1L;
            Long bookId = 1L;
            var user = UserEntity.builder()
                    .id(1L)
                    .email("user@example.com")
                    .password("password")
                    .name("User")
                    .build();
            var rental = RentalEntity.builder()
                    .id(rentalId)
                    .book(bookMap.get(bookId))
                    .user(user)
                    .checkOutDate(LocalDateTime.now())
                    .returnDate(null)
                    .build();

            when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));

            // act
            RentalEntity result = rentalService.getRental(rentalId);

            // assert
            assertNotNull(result);
            assertEquals(rentalId, result.getId());
        }

        @Test
        void getRental_WithUserDetails_ReturnsExpectedResult() {
            // arrange
            Long rentalId = 1L;
            Long bookId = 1L;
            var user = UserEntity.builder()
                    .id(1L)
                    .email("user@example.com")
                    .password("password")
                    .name("User")
                    .build();
            var rental = RentalEntity.builder()
                    .id(rentalId)
                    .book(bookMap.get(bookId))
                    .user(user)
                    .checkOutDate(LocalDateTime.now())
                    .returnDate(null)
                    .build();
            var accessToken = "access-token";
            var principal = new JwtAuthenticationPrincipal(user.getId(), user.getEmail());
            var authPrincipal = new JwtAuthenticationToken(principal, accessToken, UserAuthority.USER_DEFAULT_AUTHORITIES);

            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
            when(rentalRepository.findByIdAndUserId(rentalId, user.getId())).thenReturn(Optional.of(rental));

            // act
            RentalEntity result = rentalService.getRental(rentalId, authPrincipal);

            // assert
            assertNotNull(result);
            assertEquals(rentalId, result.getId());
        }
    }

    @Nested
    class CreateCheckOutTests {
        @Test
        void createCheckOut_RentalAvailable_ReturnsCheckedOutRental() {
            // arrange
            Long bookId = 3L;
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
                    .returnDate(null)
                    .build();
            var accessToken = "access-token";
            var principal = new JwtAuthenticationPrincipal(user.getId(), user.getEmail());
            var authPrincipal = new JwtAuthenticationToken(principal, accessToken, UserAuthority.USER_DEFAULT_AUTHORITIES);

            var req = RentalCheckOutRequest.builder()
                    .bookId(targetBook.getId())
                    .build();

            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
            when(bookRepository.findById(req.getBookId())).thenReturn(Optional.of(targetBook));
            when(rentalRepository.save(any(RentalEntity.class))).thenReturn(rental);

            // act
            RentalEntity result = rentalService.createCheckOut(req, authPrincipal);

            // assert
            assertNotNull(result);
            assertEquals(targetBook.getId(), result.getBook().getId());
            assertEquals(BookStatus.CHECKED_OUT, result.getBook().getStatus());
            assertEquals(user.getId(), result.getUser().getId());
        }

        @Test
        void createCheckOut_WhenBookAlreadyCheckedOut_ThrowRentalNotAvailableException() {
            // arrange
            Long bookId = 6L;
            var targetBook = bookMap.get(bookId);
            var user = UserEntity.builder()
                    .id(1L)
                    .email("user@example.com")
                    .password("password")
                    .name("User")
                    .build();
            var accessToken = "access-token";
            var principal = new JwtAuthenticationPrincipal(user.getId(), user.getEmail());
            var authPrincipal = new JwtAuthenticationToken(principal, accessToken, UserAuthority.USER_DEFAULT_AUTHORITIES);

            var req = RentalCheckOutRequest.builder()
                    .bookId(targetBook.getId())
                    .build();

            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
            when(bookRepository.findById(req.getBookId())).thenReturn(Optional.of(targetBook));

            // act and assert
            assertThrows(RentalNotAvailableException.class, () -> {
                rentalService.createCheckOut(req, authPrincipal);
            });
        }
    }

    @Nested
    class CreateReturnTests {
        @Test
        void createReturn_WhenBookAlreadyCheckedOut_ReturnsRentalResult() {
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
                    .returnDate(null)
                    .build();
            var accessToken = "access-token";
            var principal = new JwtAuthenticationPrincipal(user.getId(), user.getEmail());
            var authPrincipal = new JwtAuthenticationToken(principal, accessToken, UserAuthority.USER_DEFAULT_AUTHORITIES);

            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
            when(rentalRepository.findById(rental.getId())).thenReturn(Optional.of(rental));
            when(rentalRepository.save(any(RentalEntity.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));
            when(bookRepository.save(any(BookEntity.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // act
            RentalEntity result = rentalService.createReturn(rental.getId(), authPrincipal);

            // assert
            assertNotNull(result);
            assertEquals(rental.getId(), result.getId());
            assertEquals(targetBook.getId(), result.getBook().getId());
            assertEquals(BookStatus.AVAILABLE, result.getBook().getStatus());
        }

        @Test
        void createReturn_WhenBookNotCheckedOut_ThrowNotAllowedException() {
            // arrange
            Long bookId = 2L;
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
                    .returnDate(null)
                    .build();
            var accessToken = "access-token";
            var principal = new JwtAuthenticationPrincipal(user.getId(), user.getEmail());
            var authPrincipal = new JwtAuthenticationToken(principal, accessToken, UserAuthority.USER_DEFAULT_AUTHORITIES);

            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
            when(rentalRepository.findById(rental.getId())).thenReturn(Optional.of(rental));

            // act and assert
            assertThrows(RentalReturnNotAllowedException.class, () -> {
                rentalService.createReturn(rental.getId(), authPrincipal);
            });
        }
    }
}
