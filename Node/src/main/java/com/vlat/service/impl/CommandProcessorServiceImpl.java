package com.vlat.service.impl;

import com.vlat.bot.BotCommands;
import com.vlat.bot.service.BotCommandsService;
import com.vlat.entity.enums.BotUserState;
import com.vlat.entity.BotUser;
import com.vlat.service.BotUserService;
import com.vlat.kafkaMessage.SearchMessage;
import com.vlat.service.CommandProcessorService;
import com.vlat.service.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommandProcessorServiceImpl implements CommandProcessorService {

    private final BotCommandsService botCommandsService;
    private final ProducerService producerService;

    @Override
    public String help() {
        return botCommandsService.getAllCommandsInfo();
    }

    @Override
    public String start() {
        return "Вас приветствует Анонимка - телеграм-бот для анонимного общения разных людей.\nСписок команд: /help\n\n/search - запустить поиск";
    }

    @Override
    public String search(BotUser botUser) {
        BotUserState state = botUser.getState();
        if(state == BotUserState.IDLE){
            String userChatId = botUser.getChatId();

            SearchMessage searchMessage = new SearchMessage(userChatId, BotCommands.SEARCH);
            producerService.produceSearchMessage(searchMessage);

            return "Идёт поиск собеседника...";
        }
        else if(state == BotUserState.IN_CONVERSATION){
            return "У вас уже есть собеседник.\nДля поиска следующего используйте /next\nДля остановки используйте /stop";
        }
        else{
            return "Вы уже находитесь в поиске. Для остановки используйте /stop";
        }
    }

    @Override
    public String stop(BotUser botUser) {
        if(botUser.getState() != BotUserState.IDLE){
            String userChatId = botUser.getChatId();

            SearchMessage searchMessage = new SearchMessage(userChatId, BotCommands.STOP);
            producerService.produceSearchMessage(searchMessage);

            return "Остановка...";
        }
        return null;
    }

    @Override
    public String next(BotUser botUser) {
        BotUserState state = botUser.getState();
        if(state == BotUserState.IN_CONVERSATION){
            String userChatId = botUser.getChatId();

            SearchMessage searchMessage = new SearchMessage(userChatId, BotCommands.NEXT);
            producerService.produceSearchMessage(searchMessage);

            return "Поиск следующего собеседника...";

        }else if(state == BotUserState.IN_SEARCH){
            return "Вы уже находитесь в поиске. Для отмены используйте /stop";
        }
        return "Вы не находитесь в диалоге. Для поиска собеседника используйте /search";
    }

    @Override
    public BotCommands parseCommand(String command) {
        return botCommandsService.getBotCommand(command);
    }
}
