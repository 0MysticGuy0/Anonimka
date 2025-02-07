package com.vlat.service.impl;

import com.vlat.bot.BotAnswers;
import com.vlat.bot.BotCommands;
import com.vlat.entity.BotUser;
import com.vlat.kafkaMessage.SearchMessage;
import com.vlat.service.AnswerGenerationService;
import com.vlat.service.BotUserService;
import com.vlat.service.SearchMessageProcessorService;
import com.vlat.service.SearcherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import static com.vlat.bot.BotCommands.*;

@Service
@RequiredArgsConstructor
@Log4j
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
            log.error(String.format("-=-=-=-=-=-=-| RECEIVED UNKNOWN(unhandled) COMMAND IN SearchMessageProcessorService: %s", command));
            answerGenerationService.createAnswer(botUser, BotAnswers.ERROR);
        }
    }
}
