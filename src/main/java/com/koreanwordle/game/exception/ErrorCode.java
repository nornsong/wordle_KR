package com.koreanwordle.game.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    GAME_NOT_FOUND(HttpStatus.NOT_FOUND, "게임을 찾을 수 없습니다."),
    WORD_NOT_FOUND(HttpStatus.NOT_FOUND, "등록된 단어가 없습니다."),
    INVALID_WORD_LENGTH(HttpStatus.BAD_REQUEST, "제출 단어의 길이가 정답 단어와 다릅니다."),
    INVALID_WORD_FORMAT(HttpStatus.BAD_REQUEST, "제출 단어는 완성형 한글만 입력할 수 있습니다."),
    WORD_NOT_IN_DICTIONARY(HttpStatus.BAD_REQUEST, "사전에 없는 단어입니다."),
    GAME_ALREADY_FINISHED(HttpStatus.BAD_REQUEST,"이미 종료된 게임입니다."),
    ATTEMPT_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "제출 횟수를 모두 사용했습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus status() {
        return status;
    }

    public String message() {
        return message;
    }
}