package com.example.bookrentalshop.support.exception.rental;

public class RentalAlreadyReturnedException extends RuntimeException {

    public RentalAlreadyReturnedException(Long id) {
        super("Rental with ID " + id + " has already been returned.");
    }
}
