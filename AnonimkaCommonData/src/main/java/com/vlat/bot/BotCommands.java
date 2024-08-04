package com.vlat.bot;

import lombok.Getter;

public enum BotCommands {
    START("/start","запустить бота"),
    HELP("/help","список команд"),
    SEARCH("/search","начать поиск"),
    STOP("/stop","остановить поиск/диалог");

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
