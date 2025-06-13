package com.example.bookrentalshop.support.exception.auth;

import org.springframework.security.core.AuthenticationException;

public class PasswordInvalidException extends AuthenticationException {

    public PasswordInvalidException(String message) {
        super(message);
    }

    public PasswordInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
