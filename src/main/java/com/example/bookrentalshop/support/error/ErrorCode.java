package com.example.bookrentalshop.support.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    UNEXPECTED_ERROR("ERROR-0001", ErrorLevel.ERROR, HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred"),
    RESOURCE_NOT_FOUND("ERROR-0002", ErrorLevel.ERROR, HttpStatus.NOT_FOUND, "Resource not found"),
    RESOURCE_ALREADY_EXISTS("ERROR-0003", ErrorLevel.ERROR, HttpStatus.CONFLICT, "Resource already exists"),
    AUTHENTICATION_FAILED("ERROR-0004", ErrorLevel.ERROR, HttpStatus.UNAUTHORIZED, "Authentication failed");

    private final String code;
    private final ErrorLevel level;
    private final HttpStatus httpStatus;
    private final String phrase;
}
