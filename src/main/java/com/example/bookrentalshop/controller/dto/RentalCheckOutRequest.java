package com.example.bookrentalshop.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RentalCheckOutRequest {

    @NotNull
    @Positive
    private Long bookId;
}
