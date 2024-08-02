package com.vlat.bot.service.impl;

import com.vlat.bot.BotCommands;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class BotCommandsServiceImpl implements com.vlat.bot.service.BotCommandsService {

    private final Set<String> COMMANDS_LIST;
    private final String HELP_TEXT = "Список всех доступных команд:\n";

    public BotCommandsServiceImpl() {
        COMMANDS_LIST = Arrays.stream(BotCommands.values())
                .map(BotCommands::toString)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isCommand(String text) {
        return COMMANDS_LIST.contains(text);
    }

    @Override
    public String getCommandInfo(BotCommands botCommand) {
        StringBuilder info = new StringBuilder();
        info.append(botCommand).append(" - ").append(botCommand.getDescription());

        return info.toString();
    }

    @Override
    public String getAllCommandsInfo() {
        StringBuilder helpText = new StringBuilder();
        helpText.append(HELP_TEXT);
        for(BotCommands cmd:BotCommands.values()){
            helpText.append(getCommandInfo(cmd))
                    .append("\n");
        }
        return helpText.toString();
    }

    @Override
    public BotCommands getBotCommand(String textCommand) {
        for(BotCommands cmd: BotCommands.values()){
            if(cmd.toString().equals(textCommand)){
                return cmd;
            }
        }
        return null;
    }
}
