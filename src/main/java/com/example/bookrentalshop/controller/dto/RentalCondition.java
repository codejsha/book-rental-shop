package com.example.bookrentalshop.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RentalCondition {

    private Long bookId;

    private Long userId;
}
