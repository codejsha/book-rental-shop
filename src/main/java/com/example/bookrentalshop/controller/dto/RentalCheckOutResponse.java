package com.example.bookrentalshop.controller.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RentalCheckOutResponse {

    private Long bookId;

    private Long userId;

    private LocalDateTime checkOutDate;
}
