package com.example.bookrentalshop.support.exception.resource;

public class RentalNotFoundException extends ResourceNotFoundException {

    public RentalNotFoundException(Long id) {
        super("Rental not found with id: " + id);
    }
}
