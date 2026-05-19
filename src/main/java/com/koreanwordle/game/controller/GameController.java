package com.koreanwordle.game.controller;

import com.koreanwordle.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wordleKR")
public class GameController {

    private final GameService game;

    // 오늘의 게임 생성


    // 게임 생성
    @PostMapping("/createGame")
    public ResponseEntity<Void> getGame() {
        game.getNewGame();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
