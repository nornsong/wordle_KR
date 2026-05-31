package com.koreanwordle.game.controller;

import com.koreanwordle.game.dto.GameResponse;
import com.koreanwordle.game.dto.GuessRequest;
import com.koreanwordle.game.dto.GuessResponse;
import com.koreanwordle.game.service.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wordleKR")
public class GameController {

    private final GameService gameService;

    // 오늘의 게임 생성
    @GetMapping("/dailyGame")
    public ResponseEntity<GameResponse> getDailyGame() {
        GameResponse response = gameService.getCreateDailyGame();
        return ResponseEntity.ok(response);
    }

    // 게임 생성
    @PostMapping("/createGame")
    public ResponseEntity<GameResponse> getCreateRandomGame() {
        GameResponse response = gameService.getCreateRandomGame();
        return ResponseEntity.ok(response);
    }
    
    // 사용자 정답 제출
    @PostMapping("/submitAnswer")
    public ResponseEntity<GuessResponse> submitAnswer(@Valid @RequestBody GuessRequest request) {
        GuessResponse response = gameService.submitAnswer(request.gameId(), request.submittedWord());
        return ResponseEntity.ok(response);
    }
}
