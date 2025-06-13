package com.example.bookrentalshop.support.exception.resource;

public class BookNotFoundException extends ResourceNotFoundException {

    public BookNotFoundException(Long id) {
        super("Book not found with id: " + id);
    }
}
