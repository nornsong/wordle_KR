package com.koreanwordle.game.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public record ErrorResponse(
        String code,
        String message
) {
    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(
                errorCode.name(),
                errorCode.message()
        );
    }
}
