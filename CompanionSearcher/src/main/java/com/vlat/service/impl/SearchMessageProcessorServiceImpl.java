package com.vlat.service.impl;

import com.vlat.bot.BotCommands;
import com.vlat.entity.BotUser;
import com.vlat.kafkaMessage.SearchMessage;
import com.vlat.service.AnswerGenerationService;
import com.vlat.service.BotUserService;
import com.vlat.service.SearchMessageProcessorService;
import com.vlat.service.SearcherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.vlat.bot.BotCommands.*;

@Service
@RequiredArgsConstructor
public class SearchMessageProcessorServiceImpl implements SearchMessageProcessorService {

    private final SearcherService searcherService;
    private final AnswerGenerationService answerGenerationService;
    private final BotUserService botUserService;

    @Override
    public void processSearchMessage(SearchMessage searchMessage) {
        String userChatId = searchMessage.getUserChatId();
        BotCommands command = searchMessage.getCommand();
        BotUser botUser = botUserService.getUser(userChatId);

        if(command == SEARCH){
            searcherService.search(botUser);
        }
        else if(command == STOP){
            searcherService.stop(botUser);
        }
        else if(command == NEXT){
            searcherService.next(botUser);
        }
        else{
            System.out.println("-=-=-=-=-=-=-| RECEIVED UNKNOWN COMMAND IN SearchMessageProcessorService");
            answerGenerationService.createAnswer(botUser,
                    "Ошибка! Для отмены используйте /stop");
        }
    }
}
