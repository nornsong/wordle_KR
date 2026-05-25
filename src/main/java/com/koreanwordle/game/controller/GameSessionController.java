package com.koreanwordle.game.controller;

import com.koreanwordle.game.dto.GameSessionResponse;
import com.koreanwordle.game.dto.GuessRequest;
import com.koreanwordle.game.dto.GuessResponse;
import com.koreanwordle.game.service.GameSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wordleKR")
public class GameSessionController {

    private final GameSessionService gameSessionService;

    // 오늘의 게임 생성
    @GetMapping("/dailyGame")
    public ResponseEntity<GameSessionResponse> getDailyGame(@RequestBody String userId) {
        gameSessionService.getCreateDailyGame(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 게임 생성
    @PostMapping("/createGame")
    public ResponseEntity<GameSessionResponse> getCreateRandomGame(@RequestBody String userId) {
        gameSessionService.getCreateRandomGame(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    // 사용자 정답 제출
    @PostMapping("/submitAnswer")
    public ResponseEntity<GuessResponse> submitAnswer(@RequestBody GuessRequest request) {
        GuessResponse response = gameSessionService.submitAnswer(request.userId(), request.sessionId(), request.submittedWord());
        return ResponseEntity.ok(response);
    }
    
}
