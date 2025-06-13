package com.example.bookrentalshop.controller.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RentalGetResponse {

    private Long id;

    private Long bookId;

    private String bookTitle;

    private Long userId;

    private String userName;

    private LocalDateTime checkOutDate;

    private LocalDateTime returnDate;
}
