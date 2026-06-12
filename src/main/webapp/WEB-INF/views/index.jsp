<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>하루낱말</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/css/k-wordle.css">
</head>
<body>
<main class="app-shell">
    <header class="game-header">
        <div class="brand">
            <span>하루낱말</span>
        </div>
        <div class="header-actions">
            <button id="helpButton" type="button" class="icon-button" title="도움말" aria-label="도움말">?</button>
            <button type="button" class="icon-button" title="통계" aria-label="통계">▥</button>
        </div>
    </header>

    <section class="game-panel" aria-label="K-Wordle 게임 영역">
        <div id="toast" class="toast" role="status" aria-live="polite"></div>

        <div class="mode-bar" aria-label="게임 선택">
            <button id="dailyGameButton" type="button" class="mode-button active">오늘의 문제</button>
            <button id="randomGameButton" type="button" class="mode-button">랜덤 문제</button>
        </div>

        <div id="board" class="board" aria-label="정답 입력 결과 보드"></div>

        <form id="guessForm" class="guess-form" autocomplete="off">
            <input
                    id="guessInput"
                    class="guess-input"
                    name="submittedWord"
                    type="text"
                    inputmode="text"
                    maxlength="3"
                    placeholder="세 글자 단어"
                    aria-label="제출 단어">
            <button type="submit" class="submit-button">ENTER</button>
        </form>
    </section>

    <section class="keyboard" aria-label="한글 자모 키보드">
        <div class="key-row">
            <button type="button" data-key="ㅂ">ㅂ</button>
            <button type="button" data-key="ㅈ">ㅈ</button>
            <button type="button" data-key="ㄷ">ㄷ</button>
            <button type="button" data-key="ㄱ">ㄱ</button>
            <button type="button" data-key="ㅅ">ㅅ</button>
            <button type="button" data-key="ㅛ">ㅛ</button>
            <button type="button" data-key="ㅕ">ㅕ</button>
            <button type="button" data-key="ㅑ">ㅑ</button>
            <button type="button" data-key="ㅐ">ㅐ</button>
            <button type="button" data-key="ㅔ">ㅔ</button>
        </div>
        <div class="key-row">
            <button type="button" data-key="ㅁ">ㅁ</button>
            <button type="button" data-key="ㄴ">ㄴ</button>
            <button type="button" data-key="ㅇ">ㅇ</button>
            <button type="button" data-key="ㄹ">ㄹ</button>
            <button type="button" data-key="ㅎ">ㅎ</button>
            <button type="button" data-key="ㅗ">ㅗ</button>
            <button type="button" data-key="ㅓ">ㅓ</button>
            <button type="button" data-key="ㅏ">ㅏ</button>
            <button type="button" data-key="ㅣ">ㅣ</button>
        </div>
        <div class="key-row">
            <button type="button" class="wide" data-enter="true">ENTER</button>
            <button type="button" data-key="ㅋ">ㅋ</button>
            <button type="button" data-key="ㅌ">ㅌ</button>
            <button type="button" data-key="ㅊ">ㅊ</button>
            <button type="button" data-key="ㅍ">ㅍ</button>
            <button type="button" data-key="ㅠ">ㅠ</button>
            <button type="button" data-key="ㅜ">ㅜ</button>
            <button type="button" data-key="ㅡ">ㅡ</button>
            <button type="button" data-backspace="true" aria-label="지우기">⌫</button>
        </div>
    </section>
</main>

<div id="helpModal" class="help-modal" role="dialog" aria-modal="true" aria-labelledby="helpTitle" hidden>
    <div class="help-dialog">
        <div class="help-header">
            <h2 id="helpTitle">게임 안내</h2>
            <button id="helpCloseButton" type="button" class="icon-button" title="닫기" aria-label="닫기">×</button>
        </div>
        <div class="help-content">
            <p>세 글자 낱말을 입력해 정답을 맞혀 보세요.</p>
            <p>각 칸 아래의 세 줄은 순서대로 초성, 중성, 종성 힌트입니다.</p>
            <div class="help-list">
                <span><i class="dot correct"></i>초록: 위치와 자모가 모두 맞음</span>
                <span><i class="dot present"></i>노랑: 자모는 있지만 위치가 다름</span>
                <span><i class="dot absent"></i>회색: 정답에 없는 자모</span>
            </div>
        </div>
    </div>
</div>

<script src="/js/k-wordle.js"></script>
</body>
</html>
