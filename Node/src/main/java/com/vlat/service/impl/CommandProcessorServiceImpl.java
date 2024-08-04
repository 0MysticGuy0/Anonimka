package com.vlat.service.impl;

import com.vlat.bot.BotCommands;
import com.vlat.bot.service.BotCommandsService;
import com.vlat.botUser.enums.BotUserState;
import com.vlat.entity.BotUser;
import com.vlat.service.BotUserService;
import com.vlat.service.CommandProcessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommandProcessorServiceImpl implements CommandProcessorService {

    private final BotCommandsService botCommandsService;
    private final BotUserService botUserService;

    @Override
    public String help() {
        return botCommandsService.getAllCommandsInfo();
    }

    @Override
    public String start() {
        return "Вас приветствует Анонимка - телеграм-бот для анонимного общения разных людей.\nСписок команд: /help";
    }

    @Override
    public String search(BotUser botUser) {
        if(botUser.getState() == BotUserState.IDLE){
            botUser.setState(BotUserState.IN_SEARCH);
            botUserService.saveUser(botUser);

            return "Идёт поиск собеседника...";
        }
        else{
            return "Вы уже находитесь в поиске или диалоге. Для остановки используйте /stop";
        }
    }

    @Override
    public String stop(BotUser botUser) {
        if(botUser.getState() != BotUserState.IDLE){
            botUser.setState(BotUserState.IDLE);
            botUserService.saveUser(botUser);

            return "Остановка...";
        }
        return null;
    }

    @Override
    public BotCommands parseCommand(String command) {
        return botCommandsService.getBotCommand(command);
    }
}
