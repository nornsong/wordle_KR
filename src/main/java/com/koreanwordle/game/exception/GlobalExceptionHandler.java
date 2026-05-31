package com.koreanwordle.game.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(
            CustomException e
    ) {
        ErrorCode errorCode = e.errorCode();

        return ResponseEntity
                .status(errorCode.status())
                .body(ErrorResponse.from(errorCode));
    }
}
