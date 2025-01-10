package com.vlat.service.impl;

import com.vlat.bot.BotAnswers;
import com.vlat.bot.BotCommands;
import com.vlat.entity.enums.BotUserState;
import com.vlat.entity.BotUser;
import com.vlat.kafkaMessage.*;
import com.vlat.kafkaMessage.enums.FileMessageTypes;
import com.vlat.service.BotUserService;
import com.vlat.service.CommandProcessorService;
import com.vlat.service.MessageProcessorService;
import com.vlat.service.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.vlat.bot.BotCommands.*;

@Service
@RequiredArgsConstructor
@Log4j
public class MessageProcessorServiceImpl implements MessageProcessorService {

    private  final ProducerService producerService;
    private final CommandProcessorService commandProcessorService;
    private final BotUserService botUserService;

    @Override
    @Async
    public void processTextMessage(TextMessage textMessage) {

        String authorChatId = textMessage.getAuthorId();
        String text = textMessage.getText();
        Integer messageId = textMessage.getMessageId();
        Integer replyToMessageId = textMessage.getReplyToMessageId();
        String senderChatId = textMessage.getAuthorId();
        String receiverChatId = senderChatId ;

        BotUser botUser = botUserService.getUser(authorChatId);
        BotUserState userState = botUser.getState();

        if(userState != BotUserState.IN_CONVERSATION){
            senderChatId = null;
            messageId = null;
            replyToMessageId = null;
        }
        if (userState == BotUserState.IN_SEARCH){
            text = String.format("*%s*", BotAnswers.IN_SEARCH);
        }else if(userState == BotUserState.IDLE){
            text = String.format("*%s*", BotAnswers.NOT_IN_DIALOG);
        }
        else{
            BotUser companion = botUser.getCompanion();
            if(companion != null){
                receiverChatId = companion.getChatId();
            }
            else{
                text = String.format("*%s*", BotAnswers.ERROR);
                senderChatId = null;
                messageId = null;
                replyToMessageId = null;
                log.error("-=-=-=-=-=-=--=-=-| ERROR in message processor service. Companion = null, but state is IN_CONVERSATION");
            }
        }

        AnswerTextMessage answerTextMessage = new AnswerTextMessage(receiverChatId, senderChatId,replyToMessageId, messageId, text);
        producerService.produceAnswerMessage(answerTextMessage);
    }

    @Override
    @Async
    public void processCommandMessage(CommandMessage textMessage) {
        String receiverChatId = textMessage.getAuthorId();
        String command = textMessage.getCommand();
        BotUser botUser = botUserService.getUser(receiverChatId);

        String answer = processCommand(command, botUser);

        if (answer != null){
            answer = String.format("*%s*", answer);
            AnswerTextMessage answerTextMessage = new AnswerTextMessage(receiverChatId, answer);
            producerService.produceAnswerMessage(answerTextMessage);
        }
    }

    @Override
    @Async
    public void processFileMessage(FileMessage fileMessage) {
        String authorChatId = fileMessage.getAuthorId();
        Integer replyToMessageId = fileMessage.getReplyToMessageId();
        String fileId = fileMessage.getFileId();
        FileMessageTypes fileType = fileMessage.getFileType();
        String senderChatId = fileMessage.getAuthorId();
        String receiverChatId = senderChatId;
        Integer messageId = fileMessage.getMessageId();

        BotUser botUser = botUserService.getUser(authorChatId);
        BotUserState userState = botUser.getState();

        if(userState != BotUserState.IN_CONVERSATION){
            String text = null;
            if (userState == BotUserState.IN_SEARCH){
                text = String.format("*%s*", BotAnswers.IN_SEARCH);
            }else if(userState == BotUserState.IDLE){
                text = String.format("*%s*", BotAnswers.NOT_IN_DIALOG);
            }

            AnswerTextMessage answerTextMessage = new AnswerTextMessage(receiverChatId, text);
            producerService.produceAnswerMessage(answerTextMessage);
        }
        else{
            BotUser companion = botUser.getCompanion();
            if(companion != null){
                receiverChatId = companion.getChatId();
            }
            else{
                String text = String.format("*%s*", BotAnswers.ERROR);
                AnswerTextMessage answerTextMessage = new AnswerTextMessage(receiverChatId, text);
                producerService.produceAnswerMessage(answerTextMessage);
                log.error("-=-=-=-=-=-=--=-=-| ERROR in message processor service in file-process. Companion = null, but state is IN_CONVERSATION");
                return;
            }

            //TODO проверка подписки если надо отправить фото или видео

            AnswerFileMessage answerFileMessage = new AnswerFileMessage(receiverChatId, senderChatId, replyToMessageId, messageId, fileId, fileType);
            producerService.produceAnswerMessage(answerFileMessage);
        }
    }


    private String processCommand(String textCommand, BotUser botUser){

        BotCommands command = commandProcessorService.parseCommand(textCommand);

        if( command == START){
            return commandProcessorService.start();
        }
        else if(command == HELP){
            return commandProcessorService.help();
        }
        else if(command == SEARCH){
            return commandProcessorService.search(botUser);
        }
        else if(command == STOP){
            return commandProcessorService.stop(botUser);
        }
        else if(command == NEXT){
            return commandProcessorService.next(botUser);
        }
        else{
            return BotAnswers.UNKNOWN_COMMAND;
        }
    }
}
