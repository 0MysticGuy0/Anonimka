package com.vlat.bot;

import lombok.Getter;

public enum BotCommands {
    SEARCH("/search","\uD83D\uDD0E начать поиск"),
    NEXT("/next","➡\uFE0F следующий собеседник"),
    STOP("/stop","⛔ остановить поиск/диалог"),
    HELP("/help","❓ список команд"),
    START("/start","\uD83D\uDC4B приветствие"),
    ;

    @Getter
    private final String description;
    private final String syntax;

    private BotCommands(String syntax, String description) {
        this.description = description;
        this.syntax = syntax;
    }

    @Override
    public String toString() {
        return syntax;
    }


}
