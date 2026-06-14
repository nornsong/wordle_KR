const WORD_LENGTH = 3;
const board = document.getElementById("board");
const toast = document.getElementById("toast");
const guessForm = document.getElementById("guessForm");
const guessInput = document.getElementById("guessInput");
const dailyGameButton = document.getElementById("dailyGameButton");
const randomGameButton = document.getElementById("randomGameButton");
const helpButton = document.getElementById("helpButton");
const helpCloseButton = document.getElementById("helpCloseButton");
const helpModal = document.getElementById("helpModal");
const resultModal = document.getElementById("resultModal");
const resultCloseButton = document.getElementById("resultCloseButton");
const resultMessage = document.getElementById("resultMessage");
const resultAnswer = document.getElementById("resultAnswer");
const resultDefinition = document.getElementById("resultDefinition");

let currentGame = null;
let currentRow = 0;
let maxAttempts = 6;
let isComposing = false;

const statePriority = {
    ABSENT: 1,
    PRESENT: 2,
    CORRECT: 3
};

const CHOSEONG = ["ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"];
const JUNGSEONG = ["ㅏ", "ㅐ", "ㅑ", "ㅒ", "ㅓ", "ㅔ", "ㅕ", "ㅖ", "ㅗ", "ㅘ", "ㅙ", "ㅚ", "ㅛ", "ㅜ", "ㅝ", "ㅞ", "ㅟ", "ㅠ", "ㅡ", "ㅢ", "ㅣ"];
const JONGSEONG = ["", "ㄱ", "ㄲ", "ㄳ", "ㄴ", "ㄵ", "ㄶ", "ㄷ", "ㄹ", "ㄺ", "ㄻ", "ㄼ", "ㄽ", "ㄾ", "ㄿ", "ㅀ", "ㅁ", "ㅂ", "ㅄ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"];
const COMBINED_VOWELS = {
    "ㅗㅏ": "ㅘ",
    "ㅗㅐ": "ㅙ",
    "ㅗㅣ": "ㅚ",
    "ㅜㅓ": "ㅝ",
    "ㅜㅔ": "ㅞ",
    "ㅜㅣ": "ㅟ",
    "ㅡㅣ": "ㅢ"
};
const COMBINED_FINALS = {
    "ㄱㅅ": "ㄳ",
    "ㄴㅈ": "ㄵ",
    "ㄴㅎ": "ㄶ",
    "ㄹㄱ": "ㄺ",
    "ㄹㅁ": "ㄻ",
    "ㄹㅂ": "ㄼ",
    "ㄹㅅ": "ㄽ",
    "ㄹㅌ": "ㄾ",
    "ㄹㅍ": "ㄿ",
    "ㄹㅎ": "ㅀ",
    "ㅂㅅ": "ㅄ"
};
const SPLIT_FINALS = {
    "ㄳ": ["ㄱ", "ㅅ"],
    "ㄵ": ["ㄴ", "ㅈ"],
    "ㄶ": ["ㄴ", "ㅎ"],
    "ㄺ": ["ㄹ", "ㄱ"],
    "ㄻ": ["ㄹ", "ㅁ"],
    "ㄼ": ["ㄹ", "ㅂ"],
    "ㄽ": ["ㄹ", "ㅅ"],
    "ㄾ": ["ㄹ", "ㅌ"],
    "ㄿ": ["ㄹ", "ㅍ"],
    "ㅀ": ["ㄹ", "ㅎ"],
    "ㅄ": ["ㅂ", "ㅅ"]
};

function initializeBoard(rows = maxAttempts) {
    board.innerHTML = "";
    board.style.gridTemplateRows = `repeat(${rows}, 62px)`;

    for (let row = 0; row < rows; row += 1) {
        for (let col = 0; col < WORD_LENGTH; col += 1) {
            const tile = document.createElement("div");
            tile.className = "tile";
            tile.dataset.row = String(row);
            tile.dataset.col = String(col);
            board.appendChild(tile);
        }
    }
}

function resetKeyboardState() {
    document.querySelectorAll(".keyboard [data-key]").forEach((key) => {
        key.dataset.hint = "";
        key.classList.remove("correct", "present", "absent");
    });
}

function showToast(message, isError = false) {
    toast.textContent = message;
    toast.classList.toggle("error", isError);
    toast.classList.add("visible");

    window.clearTimeout(showToast.timer);
    showToast.timer = window.setTimeout(() => {
        toast.classList.remove("visible");
    }, 2200);
}

function openHelpModal() {
    helpModal.hidden = false;
}

function closeHelpModal() {
    helpModal.hidden = true;
}

function openResultModal(response, solved) {
    resultMessage.textContent = solved ? "정답입니다." : "시도 횟수를 모두 사용했습니다.";
    resultAnswer.textContent = response.correctAnswer || "-";
    resultDefinition.textContent = response.definition || "뜻 정보가 없습니다.";
    resultModal.hidden = false;
}

function closeResultModal() {
    resultModal.hidden = true;
}

function shouldShowHelpModalOnLoad() {
    return window.matchMedia("(max-width: 780px)").matches;
}

async function requestJson(url, options = {}) {
    const response = await fetch(url, {
        headers: {
            "Content-Type": "application/json",
            ...(options.headers || {})
        },
        ...options
    });

    if (!response.ok) {
        let message = "요청을 처리할 수 없습니다.";
        try {
            const error = await response.json();
            message = error.message || message;
        } catch (_ignored) {
            message = response.statusText || message;
        }
        throw new Error(message);
    }

    return response.json();
}

function applyGameResponse(game) {
    currentGame = game;
    currentRow = 0;
    maxAttempts = game.maxAttemptsCount || 6;
    initializeBoard(maxAttempts);
    resetKeyboardState();
    guessInput.value = "";
    guessInput.disabled = false;
    guessInput.focus();
}

async function startDailyGame() {
    const game = await requestJson("/api/wordleKR/dailyGame");
    dailyGameButton.classList.add("active");
    randomGameButton.classList.remove("active");
    applyGameResponse(game);
    showToast("오늘의 문제를 시작합니다.");
}

async function startRandomGame() {
    const game = await requestJson("/api/wordleKR/createGame", {
        method: "POST"
    });
    randomGameButton.classList.add("active");
    dailyGameButton.classList.remove("active");
    applyGameResponse(game);
    showToast("랜덤 문제를 시작합니다.");
}

function tileFor(row, col) {
    return board.querySelector(`[data-row="${row}"][data-col="${col}"]`);
}

function normalizeKoreanInput(value) {
    return value.replace(/[^가-힣]/g, "").slice(0, WORD_LENGTH);
}

function isConsonant(value) {
    return CHOSEONG.includes(value);
}

function isVowel(value) {
    return JUNGSEONG.includes(value);
}

function isHangulSyllable(value) {
    if (!value) {
        return false;
    }

    const code = value.charCodeAt(0);
    return code >= 0xac00 && code <= 0xd7a3;
}

function composeSyllable(cho, jung, jong = "") {
    const choIndex = CHOSEONG.indexOf(cho);
    const jungIndex = JUNGSEONG.indexOf(jung);
    const jongIndex = JONGSEONG.indexOf(jong);

    if (choIndex < 0 || jungIndex < 0 || jongIndex < 0) {
        return "";
    }

    return String.fromCharCode(0xac00 + (choIndex * 21 + jungIndex) * 28 + jongIndex);
}

function decomposeSyllable(value) {
    if (!isHangulSyllable(value)) {
        return null;
    }

    const offset = value.charCodeAt(0) - 0xac00;
    const choIndex = Math.floor(offset / 588);
    const jungIndex = Math.floor((offset % 588) / 28);
    const jongIndex = offset % 28;

    return {
        cho: CHOSEONG[choIndex],
        jung: JUNGSEONG[jungIndex],
        jong: JONGSEONG[jongIndex]
    };
}

function syncGuessInput(force = false) {
    if (!force && isComposing) {
        renderInputPreview(normalizeKoreanInput(guessInput.value));
        return;
    }

    const normalized = normalizeKoreanInput(guessInput.value.trim());
    if (guessInput.value !== normalized) {
        guessInput.value = normalized;
    }
    renderInputPreview(normalized);
}

function renderTileLetter(tile, letter) {
    tile.innerHTML = "";

    if (!letter) {
        return;
    }

    const letterElement = document.createElement("span");
    letterElement.className = "tile-letter";
    letterElement.textContent = letter;
    tile.appendChild(letterElement);
}

function renderInputPreview(word) {
    for (let col = 0; col < WORD_LENGTH; col += 1) {
        const tile = tileFor(currentRow, col);
        if (!tile) {
            continue;
        }

        renderTileLetter(tile, word[col] || "");
    }
}

function renderCurrentInput() {
    renderInputPreview(normalizeKoreanInput(guessInput.value));
}

function appendVirtualJamo(jamo) {
    const chars = Array.from(guessInput.value);
    const lastIndex = chars.length - 1;
    const last = chars[lastIndex];
    const lastParts = decomposeSyllable(last);

    if (isConsonant(jamo)) {
        if (lastParts && lastParts.jung) {
            if (!lastParts.jong && JONGSEONG.includes(jamo)) {
                chars[lastIndex] = composeSyllable(lastParts.cho, lastParts.jung, jamo);
            } else {
                const combinedFinal = COMBINED_FINALS[`${lastParts.jong}${jamo}`];
                if (combinedFinal) {
                    chars[lastIndex] = composeSyllable(lastParts.cho, lastParts.jung, combinedFinal);
                } else if (chars.length < WORD_LENGTH) {
                    chars.push(jamo);
                }
            }
        } else if (chars.length < WORD_LENGTH) {
            chars.push(jamo);
        }
    }

    if (isVowel(jamo)) {
        if (isConsonant(last)) {
            chars[lastIndex] = composeSyllable(last, jamo);
        } else if (lastParts && lastParts.jung) {
            if (lastParts.jong) {
                if (chars.length < WORD_LENGTH) {
                    const splitFinal = SPLIT_FINALS[lastParts.jong];
                    const carryFinal = splitFinal ? splitFinal[1] : lastParts.jong;
                    const remainFinal = splitFinal ? splitFinal[0] : "";

                    chars[lastIndex] = composeSyllable(lastParts.cho, lastParts.jung, remainFinal);
                    chars.push(composeSyllable(carryFinal, jamo));
                }
            } else {
                const combinedVowel = COMBINED_VOWELS[`${lastParts.jung}${jamo}`];
                if (combinedVowel) {
                    chars[lastIndex] = composeSyllable(lastParts.cho, combinedVowel);
                } else if (chars.length < WORD_LENGTH) {
                    chars.push(jamo);
                }
            }
        } else if (chars.length < WORD_LENGTH) {
            chars.push(jamo);
        }
    }

    guessInput.value = chars.join("");
    renderCurrentInput();
    guessInput.focus();
}

function normalizeHintName(hint) {
    return String(hint || "ABSENT").toLowerCase();
}

function strongestHint(jamos) {
    return jamos.reduce((strongest, jamo) => {
        const current = jamo.hint || "ABSENT";
        return statePriority[current] > statePriority[strongest] ? current : strongest;
    }, "ABSENT");
}

function renderGuess(response, submittedWord) {
    const row = currentRow;

    response.results.forEach((result, col) => {
        const tile = tileFor(row, col);
        if (!tile) {
            return;
        }

        const jamos = result.jamos || [];
        const tileState = normalizeHintName(strongestHint(jamos));

        tile.className = `tile filled ${tileState}`;
        tile.style.setProperty("--tile-delay", `${col * 130}ms`);
        renderTileLetter(tile, result.syllable || submittedWord[col] || "");

        const strip = document.createElement("div");
        strip.className = "jamo-strip";
        jamos.forEach((jamo, jamoIndex) => {
            const marker = document.createElement("span");
            marker.className = `jamo-state ${normalizeHintName(jamo.hint)}`;
            marker.style.setProperty("--jamo-delay", `${col * 130 + 260 + jamoIndex * 90}ms`);
            marker.title = `${jamo.type}: ${jamo.value} / ${jamo.hint}`;
            strip.appendChild(marker);
            updateKeyboardKey(jamo.value, jamo.hint);
        });
        tile.appendChild(strip);
    });

    currentRow += 1;
}

function updateKeyboardKey(value, hint) {
    const key = document.querySelector(`[data-key="${CSS.escape(value)}"]`);
    if (!key) {
        return;
    }

    const nextPriority = statePriority[hint] || 0;
    const currentHint = key.dataset.hint || "";
    const currentPriority = statePriority[currentHint] || 0;

    if (nextPriority >= currentPriority) {
        key.dataset.hint = hint;
        key.classList.remove("correct", "present", "absent");
        key.classList.add(normalizeHintName(hint));
    }
}

async function submitGuess(event) {
    event.preventDefault();

    if (!currentGame) {
        showToast("게임을 먼저 시작해 주세요.", true);
        return;
    }

    syncGuessInput(true);
    const submittedWord = guessInput.value;

    if (submittedWord.length !== WORD_LENGTH) {
        showToast(`${WORD_LENGTH}글자 단어를 입력해 주세요.`, true);
        return;
    }

    try {
        const response = await requestJson("/api/wordleKR/submitAnswer", {
            method: "POST",
            body: JSON.stringify({
                gameId: currentGame.gameId,
                submittedWord
            })
        });

        renderGuess(response, submittedWord);
        guessInput.value = "";

        if (response.correct) {
            showToast("정답입니다.");
            guessInput.disabled = true;
            openResultModal(response, true);
        } else if (currentRow >= maxAttempts) {
            showToast("시도 횟수를 모두 사용했습니다.", true);
            guessInput.disabled = true;
            openResultModal(response, false);
        } else {
            showToast("힌트를 확인해 보세요.");
        }
    } catch (error) {
        showToast(error.message, true);
    }
}

function handleKeyboardClick(event) {
    const button = event.target.closest("button");
    if (!button || guessInput.disabled) {
        return;
    }

    if (button.dataset.enter === "true") {
        guessForm.requestSubmit();
        return;
    }

    if (button.dataset.backspace === "true") {
        guessInput.value = guessInput.value.slice(0, -1);
        renderCurrentInput();
        guessInput.focus();
        return;
    }

    if (button.dataset.key) {
        appendVirtualJamo(button.dataset.key);
    }
}

guessForm.addEventListener("submit", submitGuess);
guessInput.addEventListener("compositionstart", () => {
    isComposing = true;
});
guessInput.addEventListener("compositionend", () => {
    isComposing = false;
    syncGuessInput(true);
});
guessInput.addEventListener("input", (event) => {
    if (event.isComposing || isComposing) {
        renderCurrentInput();
        return;
    }

    syncGuessInput();
});
dailyGameButton.addEventListener("click", () => startDailyGame().catch((error) => showToast(error.message, true)));
randomGameButton.addEventListener("click", () => startRandomGame().catch((error) => showToast(error.message, true)));
document.querySelector(".keyboard").addEventListener("click", handleKeyboardClick);
helpButton.addEventListener("click", openHelpModal);
helpCloseButton.addEventListener("click", closeHelpModal);
helpModal.addEventListener("click", (event) => {
    if (event.target === helpModal) {
        closeHelpModal();
    }
});
resultCloseButton.addEventListener("click", closeResultModal);
resultModal.addEventListener("click", (event) => {
    if (event.target === resultModal) {
        closeResultModal();
    }
});

initializeBoard();
if (shouldShowHelpModalOnLoad()) {
    openHelpModal();
}
startDailyGame().catch((error) => showToast(error.message, true));

