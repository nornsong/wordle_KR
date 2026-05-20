package com.koreanwordle.game.controller;

import com.koreanwordle.game.dto.GuessRequest;
import com.koreanwordle.game.dto.GuessResponse;
import com.koreanwordle.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wordleKR")
public class GameController {

    private final GameService game;

    // 오늘의 게임 생성
    @GetMapping("/dailyGame")
    public ResponseEntity<String> getDailyGame() {
        String dailyWord = game.getDailyGame();
        return ResponseEntity.ok(dailyWord);
    }

    // 게임 생성
    @PostMapping("/createGame")
    public ResponseEntity<Void> getCreateNewGame() {
        game.getCreateNewGame();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    // 사용자 정답 제출
    @PostMapping("/submitAnswer")
    public ResponseEntity<GuessResponse> submitAnswer(@RequestBody GuessRequest request) {
        GuessResponse response = game.submitAnswer(request.gameId(), request.submittedWord());
        return ResponseEntity.ok(response);
    }
    
}
