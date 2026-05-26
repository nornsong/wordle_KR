package com.koreanwordle.game.controller;

import com.koreanwordle.game.dto.GameResponse;
import com.koreanwordle.game.dto.GuessRequest;
import com.koreanwordle.game.dto.GuessResponse;
import com.koreanwordle.game.service.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wordleKR")
public class GameSessionController {

    private final GameService gameService;

    // 오늘의 게임 생성
    @GetMapping("/dailyGame")
    public ResponseEntity<GameResponse> getDailyGame() {
        gameService.getCreateDailyGame();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 게임 생성
    @PostMapping("/createGame")
    public ResponseEntity<GameResponse> getCreateRandomGame() {
        gameService.getCreateRandomGame();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    // 사용자 정답 제출
    @PostMapping("/submitAnswer")
    public ResponseEntity<GuessResponse> submitAnswer(@Valid @RequestBody GuessRequest request) {
        GuessResponse response = gameService.submitAnswer(request.gameId(), request.submittedWord());
        return ResponseEntity.ok(response);
    }
    
}
