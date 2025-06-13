package com.example.bookrentalshop.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum BookStatus {

    UNKNOWN(Constants.UNKNOWN, "Unknown"),
    AVAILABLE(Constants.AVAILABLE, "Ready for Rental"),
    CHECKED_OUT(Constants.CHECKED_OUT, "Currently Checked Out");

    private final String status;
    private final String description;

    public static final Map<String, BookStatus> STATUS_MAP = Stream.of(BookStatus.values())
            .collect(Collectors.toUnmodifiableMap(BookStatus::getStatus, Function.identity()));

    public static class Constants {
        public static final String UNKNOWN = "UNKNOWN";
        public static final String AVAILABLE = "AVAILABLE";
        public static final String CHECKED_OUT = "CHECKED_OUT";
    }
}
