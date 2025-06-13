package com.example.bookrentalshop.domain.command;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReturnBookCommand {

    private LocalDateTime returnDate;

    public static ReturnBookCommand create() {
        return ReturnBookCommand.builder()
                .returnDate(LocalDateTime.now())
                .build();
    }
}
