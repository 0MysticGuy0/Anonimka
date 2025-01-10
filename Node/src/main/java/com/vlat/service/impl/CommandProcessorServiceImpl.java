package com.vlat.service.impl;

import com.vlat.bot.BotAnswers;
import com.vlat.bot.BotCommands;
import com.vlat.bot.service.BotCommandsService;
import com.vlat.entity.enums.BotUserState;
import com.vlat.entity.BotUser;
import com.vlat.kafkaMessage.SearchMessage;
import com.vlat.service.CommandProcessorService;
import com.vlat.service.ProducerService;
import lombok.RequiredArgsConstructor;
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
        return BotAnswers.COMMAND_START;
    }

    @Override
    public String search(BotUser botUser) {
        BotUserState state = botUser.getState();
        if(state == BotUserState.IDLE){
            String userChatId = botUser.getChatId();

            SearchMessage searchMessage = new SearchMessage(userChatId, BotCommands.SEARCH);
            producerService.produceSearchMessage(searchMessage);

            return BotAnswers.COMMAND_SEARCH;
        }
        else if(state == BotUserState.IN_CONVERSATION){
            return BotAnswers.ALREADY_HAS_COMPANION;
        }
        else{
            return BotAnswers.ALREADY_IN_SEARCH;
        }
    }

    @Override
    public String stop(BotUser botUser) {
        if(botUser.getState() != BotUserState.IDLE){
            String userChatId = botUser.getChatId();

            SearchMessage searchMessage = new SearchMessage(userChatId, BotCommands.STOP);
            producerService.produceSearchMessage(searchMessage);

            return BotAnswers.COMMAND_STOP;
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

            return BotAnswers.COMMAND_NEXT;

        }else if(state == BotUserState.IN_SEARCH){
            return BotAnswers.ALREADY_IN_SEARCH;
        }
        return BotAnswers.NOT_IN_DIALOG;
    }

    @Override
    public BotCommands parseCommand(String command) {
        return botCommandsService.getBotCommand(command);
    }
}
