package com.example.bookrentalshop.domain.command;

import com.example.bookrentalshop.controller.dto.BookUpdateRequest;
import com.example.bookrentalshop.domain.constant.BookStatus;
import com.example.bookrentalshop.domain.entity.CategoryEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookUpdateCommand {

    private String author;

    private String title;

    private BookStatus status;

    private CategoryEntity category;

    public static BookUpdateCommand from(BookUpdateRequest req, BookStatus status, CategoryEntity category) {
        return BookUpdateCommand.builder()
                .author(req.getAuthor())
                .title(req.getTitle())
                .status(status)
                .category(category)
                .build();
    }

    public static BookUpdateCommand from(BookStatus status) {
        return BookUpdateCommand.builder()
                .status(status)
                .build();
    }
}
