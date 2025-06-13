package com.example.bookrentalshop.support.exception;

import com.example.bookrentalshop.support.error.ErrorCode;
import com.example.bookrentalshop.support.error.ErrorResponse;
import com.example.bookrentalshop.support.exception.resource.ResourceAlreadyExistsException;
import com.example.bookrentalshop.support.exception.resource.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex) {
        var code = ErrorCode.AUTHENTICATION_FAILED;
        return ResponseEntity.of(ErrorResponse.of(ex, code)).build();
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<?> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        var code = ErrorCode.RESOURCE_ALREADY_EXISTS;
        return ResponseEntity.of(ErrorResponse.of(ex, code)).build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        var code = ErrorCode.RESOURCE_NOT_FOUND;
        return ResponseEntity.of(ErrorResponse.of(ex, code)).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex) {
        var code = ErrorCode.UNEXPECTED_ERROR;
        ex.printStackTrace();
        return ResponseEntity.of(ErrorResponse.of(ex, code)).build();
    }
}
