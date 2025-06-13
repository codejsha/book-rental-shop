package com.example.bookrentalshop.support.exception.rental;

public class RentalReturnNotAllowedException extends RuntimeException {

    public RentalReturnNotAllowedException(String message) {
        super(message);
    }
}
