<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="하루낱말은 한글 유니코드 완성형 음절을 초성, 중성, 종성으로 분해해 자모 위치를 비교하는 세 글자 한국어 단어 추리 게임입니다.">
    <link rel="canonical" href="https://onwoorin.com/intro">
    <title>하루낱말 소개</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/css/k-wordle.css">
</head>
<body>
<main class="intro-shell">
    <header class="intro-header">
        <a class="brand intro-brand" href="/">하루낱말</a>
        <nav class="top-nav" aria-label="주요 메뉴">
            <a class="top-nav-link" href="/">게임</a>
        </nav>
    </header>

    <section class="intro-hero" aria-label="하루낱말 소개">
        <div class="intro-copy">
            <p class="intro-kicker">한글 구조로 푸는 짧은 낱말 추리</p>
            <h1>하루에 한 번, 낱말의 결을 맞혀보세요.</h1>
            <p>
                하루낱말은 세 글자 우리말을 맞히는 추리 게임입니다.
                글자 전체가 아니라 초성, 중성, 종성을 따로 비교해 한글의 구조를 더 선명하게 보여줍니다.
            </p>
        </div>

        <div class="intro-preview" aria-label="게임 미리보기">
            <div class="preview-board">
                <div class="example-tile">
                    <span>소</span>
                    <div class="jamo-strip">
                        <span class="jamo-state absent"></span>
                        <span class="jamo-state correct"></span>
                        <span class="jamo-state correct"></span>
                    </div>
                </div>
                <div class="example-tile">
                    <span>나</span>
                    <div class="jamo-strip">
                        <span class="jamo-state absent"></span>
                        <span class="jamo-state present"></span>
                        <span class="jamo-state correct"></span>
                    </div>
                </div>
                <div class="example-tile">
                    <span>무</span>
                    <div class="jamo-strip">
                        <span class="jamo-state absent"></span>
                        <span class="jamo-state absent"></span>
                        <span class="jamo-state correct"></span>
                    </div>
                </div>
            </div>
            <div class="preview-caption">
                <span>정답 예시: 항공기</span>
            </div>
            <div class="preview-note">
                <span><i class="dot correct"></i>초록: 같은 위치에 같은 자모가 있음</span>
                <span><i class="dot present"></i>노랑: 정답에 있지만 위치가 다름</span>
                <span><i class="dot absent"></i>진회색: 정답에 없는 자모</span>
            </div>
        </div>
    </section>

    <section class="intro-section" aria-label="개요">
        <h2>개요</h2>
        <p>
            짧은 빈 시간에 부담 없이 한 문제를 풀 수 있도록 만들었습니다.
            단순한 단어 암기가 아니라, 자음과 모음의 위치를 비교하며 정답에 가까워지는 흐름을 제공합니다.
        </p>
    </section>

    <section class="intro-section" aria-labelledby="hangulUnicodeTitle">
        <h2 id="hangulUnicodeTitle">한글 유니코드로 비교하는 자모 힌트</h2>
        <p>
            하루낱말은 한글 유니코드의 완성형 음절인 가부터 힣까지의 글자를 초성, 중성, 종성으로 분해합니다.
            제출한 세 글자 한국어 단어와 정답을 비교할 때 초성은 초성끼리, 중성은 중성끼리, 종성은 종성끼리 확인합니다.
            같은 위치의 같은 자모는 초록색, 정답에 있지만 위치가 다른 자모는 노란색, 정답에 없는 자모는 진회색 힌트로 표시합니다.
            이 과정을 통해 글자 전체뿐 아니라 한글을 구성하는 자음과 모음의 위치를 추리하며 정답에 가까워질 수 있습니다.
        </p>
    </section>

    <section class="intro-feature-section" aria-label="특징">
        <div class="intro-symbols" aria-hidden="true">
            <span>ㄱ</span>
            <span>ㅏ</span>
            <span>ㄴ</span>
            <span>ㅎ</span>
        </div>
        <div class="intro-features">
            <h2>특징</h2>
            <ul>
                <li>초성, 중성, 종성을 분리해서 보여주는 힌트 구조</li>
                <li>매일 새롭게 도전하는 오늘의 문제</li>
                <li>가볍게 반복할 수 있는 랜덤 문제</li>
            </ul>
        </div>
    </section>

    <section class="intro-cta" aria-label="게임 시작">
        <h2>오늘의 낱말을 시작할 준비가 됐나요?</h2>
        <p>세 글자 단어를 입력하고 첫 힌트를 확인해보세요.</p>
        <a class="seal-button" href="/">게임으로 이동</a>
    </section>
</main>
</body>
</html>


