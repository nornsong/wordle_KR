package com.koreanwordle.game.scheduler;

import com.koreanwordle.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DailyWordScheduler {

    private final GameService gameService;

    // cron : 초 분 시 일 월 요일
    // 개발중엔 임시로 fixedRate = 10000
    @Scheduled(fixedRate = 10000)
    public void createDailyWord() {
        gameService.createDailyWord();
    }
}
