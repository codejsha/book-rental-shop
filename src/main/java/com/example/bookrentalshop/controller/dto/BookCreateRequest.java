package com.example.bookrentalshop.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookCreateRequest {

    @NotBlank
    private String author;

    @NotBlank
    private String title;

    @NotBlank
    private String status;

    @NotBlank
    private String category;
}
