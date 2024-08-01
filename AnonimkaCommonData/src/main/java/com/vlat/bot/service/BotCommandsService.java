package com.vlat.bot.service;

import com.vlat.bot.BotCommands;

public interface BotCommandsService {
    boolean isCommand(String text);
    String getCommandInfo(BotCommands botCommand);
    String getAllCommandsInfo();
}
