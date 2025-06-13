package com.example.bookrentalshop.domain.service;

import com.example.bookrentalshop.controller.dto.RentalCheckOutRequest;
import com.example.bookrentalshop.controller.dto.RentalCondition;
import com.example.bookrentalshop.domain.command.BookUpdateCommand;
import com.example.bookrentalshop.domain.command.ReturnBookCommand;
import com.example.bookrentalshop.domain.constant.BookStatus;
import com.example.bookrentalshop.domain.entity.QRentalEntity;
import com.example.bookrentalshop.domain.entity.RentalEntity;
import com.example.bookrentalshop.repository.BookRepository;
import com.example.bookrentalshop.repository.RentalRepository;
import com.example.bookrentalshop.repository.UserRepository;
import com.example.bookrentalshop.support.exception.auth.UserNotFoundException;
import com.example.bookrentalshop.support.exception.rental.RentalAlreadyReturnedException;
import com.example.bookrentalshop.support.exception.rental.RentalNotAvailableException;
import com.example.bookrentalshop.support.exception.rental.RentalReturnNotAllowedException;
import com.example.bookrentalshop.support.exception.resource.BookNotFoundException;
import com.example.bookrentalshop.support.exception.resource.RentalNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<RentalEntity> getAllRentals(RentalCondition condition, Pageable pageable) {
        var predicate = createRentalPredicate(condition);
        return rentalRepository.findAll(predicate, pageable);
    }

    @Transactional(readOnly = true)
    public Page<RentalEntity> getAllRentals(RentalCondition condition, Pageable pageable, Principal principal) {
        var user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        var predicate = new BooleanBuilder();
        predicate.and(QRentalEntity.rentalEntity.user.id.eq(user.getId()));
        predicate.and(createRentalPredicate(condition));
        return rentalRepository.findAll(predicate, pageable);
    }

    private static BooleanBuilder createRentalPredicate(RentalCondition condition) {
        var predicate = new BooleanBuilder();
        if (condition.getBookId() != null) {
            predicate.and(QRentalEntity.rentalEntity.book.id.eq(condition.getBookId()));
        }
        if (condition.getUserId() != null) {
            predicate.and(QRentalEntity.rentalEntity.user.id.eq(condition.getUserId()));
        }
        return predicate;
    }

    @Transactional(readOnly = true)
    public RentalEntity getRental(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public RentalEntity getRental(Long id, Principal principal) {
        var user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        var rental = rentalRepository.findByIdAndUserId(id, user.getId());
        if (rental.isEmpty()) {
            throw new RentalNotFoundException(id);
        }
        return rental.get();
    }

    @Transactional
    public RentalEntity createCheckOut(RentalCheckOutRequest req, Principal principal) {
        var user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        var book = bookRepository.findById(req.getBookId())
                .orElseThrow(() -> new BookNotFoundException(req.getBookId()));
        if (!book.isStatusAvailable()) {
            throw new RentalNotAvailableException("Book is not available for checkout");
        }
        var rental = RentalEntity.newInstance(book, user);

        var command = BookUpdateCommand.from(BookStatus.CHECKED_OUT);
        book.update(command);
        return rentalRepository.save(rental);
    }

    @Transactional
    public RentalEntity createReturn(Long id, Principal principal) {
        var user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // set rental status to returned
        var rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException(id));
        if (rental.getReturnDate() != null) {
            throw new RentalAlreadyReturnedException(id);
        }
        if (!rental.isMyRental(user)) {
            throw new RentalNotFoundException(id);
        }
        rental.returnBook(ReturnBookCommand.create());

        // set book status to available
        var book = rental.getBook();
        if (!book.isStatusCheckedOut()) {
            throw new RentalReturnNotAllowedException("Book is not available for return");
        }
        var command = BookUpdateCommand.from(BookStatus.AVAILABLE);
        book.update(command);

        rentalRepository.save(rental);
        bookRepository.save(book);

        return rental;
    }
}
