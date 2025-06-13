package com.example.bookrentalshop.controller;

import com.example.bookrentalshop.controller.dto.*;
import com.example.bookrentalshop.domain.entity.BookEntity;
import com.example.bookrentalshop.domain.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_BOOK_READ', 'ROLE_BOOK_MANAGE', 'ROLE_ADMIN')")
    public ResponseEntity<Page<BookGetResponse>> getAllBooks(
            @ModelAttribute BookCondition condition,
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        var result = bookService.getAllBooks(condition, pageable);
        var response = result.map(BookEntity::toBookGetResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_BOOK_MANAGE', 'ROLE_ADMIN')")
    public ResponseEntity<Void> addBook(@Valid @RequestBody BookCreateRequest req) {
        var book = bookService.addBook(req);
        return ResponseEntity.created(
                URI.create("/api/v1/books/" + book.getId())).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_BOOK_READ', 'ROLE_BOOK_MANAGE', 'ROLE_ADMIN')")
    public ResponseEntity<BookGetResponse> getBook(@PathVariable Long id) {
        var book = bookService.getBook(id);
        return ResponseEntity.ok(book.toBookGetResponse());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_BOOK_MANAGE', 'ROLE_ADMIN')")
    public ResponseEntity<BookUpdateResponse> updateBook(@PathVariable Long id,
                                                         @Valid @RequestBody BookUpdateRequest req) {
        var book = bookService.updateBook(id, req);
        return ResponseEntity.ok(book.toUpdateResponse());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_BOOK_MANAGE', 'ROLE_ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
