package com.example.bookrentalshop.domain.service;

import com.example.bookrentalshop.controller.dto.BookCondition;
import com.example.bookrentalshop.controller.dto.BookCreateRequest;
import com.example.bookrentalshop.controller.dto.BookUpdateRequest;
import com.example.bookrentalshop.domain.command.BookUpdateCommand;
import com.example.bookrentalshop.domain.constant.BookStatus;
import com.example.bookrentalshop.domain.entity.BookEntity;
import com.example.bookrentalshop.domain.entity.CategoryEntity;
import com.example.bookrentalshop.domain.entity.QBookEntity;
import com.example.bookrentalshop.domain.model.CategoryModel;
import com.example.bookrentalshop.repository.BookRepository;
import com.example.bookrentalshop.support.exception.resource.BookNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryModel categoryModel;

    @Transactional(readOnly = true)
    public Page<BookEntity> getAllBooks(BookCondition condition, Pageable pageable) {
        var predicate = createBookPredicate(condition, categoryModel);
        return bookRepository.findAll(predicate, pageable);
    }

    private static BooleanBuilder createBookPredicate(BookCondition condition, CategoryModel categoryModel) {
        var predicate = new BooleanBuilder();
        if (condition.getAuthor() != null) {
            predicate.and(QBookEntity.bookEntity.author.containsIgnoreCase(condition.getAuthor()));
        }
        if (condition.getTitle() != null) {
            predicate.and(QBookEntity.bookEntity.title.containsIgnoreCase(condition.getTitle()));
        }
        if (condition.getStatus() != null) {
            BookStatus status = BookStatus.STATUS_MAP.get(condition.getStatus());
            if (status == null) {
                throw new IllegalArgumentException(condition.getStatus());
            }
            predicate.and(QBookEntity.bookEntity.status.eq(status));
        }
        if (condition.getCategory() != null) {
            CategoryEntity category = categoryModel.getNameMap().get(condition.getCategory());
            if (category == null) {
                throw new IllegalArgumentException(condition.getCategory());
            }
            predicate.and(QBookEntity.bookEntity.category.eq(category));
        }
        return predicate;
    }

    @Transactional(readOnly = true)
    public BookEntity getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Transactional
    public BookEntity addBook(BookCreateRequest req) {
        var category = categoryModel.getNameMap().get(req.getCategory());
        if (category == null) {
            throw new IllegalArgumentException(req.getCategory());
        }
        var book = BookEntity.newInstance(req, category);
        return bookRepository.save(book);
    }

    @Transactional
    public BookEntity updateBook(Long id, BookUpdateRequest req) {
        // check status
        BookStatus status = null;
        if (req.getStatus() != null) {
            status = BookStatus.STATUS_MAP.get(req.getStatus());
            if (status == null) {
                throw new IllegalArgumentException(req.getStatus());
            }
        }
        // check category
        CategoryEntity category = null;
        if (req.getCategory() != null) {
            category = categoryModel.getNameMap().get(req.getCategory());
            if (category == null) {
                throw new IllegalArgumentException(req.getCategory());
            }
        }

        var command = BookUpdateCommand.from(req, status, category);
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        book.update(command);
        return bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(Long id) {
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        bookRepository.delete(book);
    }
}
