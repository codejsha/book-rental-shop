package com.example.bookrentalshop.support.error;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.ProblemDetail;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorResponse extends ProblemDetail {

    public static ProblemDetail of(Exception ex, ErrorCode errorCode) {
        var detail = ProblemDetail.forStatus(errorCode.getHttpStatus());
        var message = ex.getMessage() != null ? ex.getMessage() : errorCode.getPhrase();
        detail.setTitle(errorCode.getCode());
        detail.setDetail(message);
        return detail;
    }
}
