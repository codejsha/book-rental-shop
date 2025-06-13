package com.example.bookrentalshop.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshRequest {

    @NotNull
    private String refreshToken;
}
