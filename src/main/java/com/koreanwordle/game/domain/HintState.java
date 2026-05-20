package com.koreanwordle.game.domain;

public enum HintState {
    CORRECT, // 정답
    PRESENT, // 는 존재하지만 위치가 다름
    ABSENT   // 단어가 존재하지 않음
}
