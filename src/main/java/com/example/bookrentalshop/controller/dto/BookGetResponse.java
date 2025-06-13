package com.example.bookrentalshop.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookGetResponse {

    private Long id;

    private String author;

    private String title;

    private String status;

    private String category;
}
