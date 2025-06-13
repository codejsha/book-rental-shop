package com.example.bookrentalshop.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookUpdateRequest {

    private String author;

    private String title;

    private String status;

    private String category;
}
