package com.vlat.service.impl;

import com.vlat.bot.BotCommands;
import com.vlat.bot.service.BotCommandsService;
import com.vlat.botUser.enums.BotUserState;
import com.vlat.entity.BotUser;
import com.vlat.kafkaMessage.*;
import com.vlat.kafkaMessage.enums.FileMessageTypes;
import com.vlat.service.BotUserService;
import com.vlat.service.CommandProcessorService;
import com.vlat.service.MessageProcessorService;
import com.vlat.service.ProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.vlat.bot.BotCommands.*;

@Service
@RequiredArgsConstructor
public class MessageProcessorServiceImpl implements MessageProcessorService {

    private  final ProducerService producerService;
    //private final BotCommandsService botCommandsService;
    private final CommandProcessorService commandProcessorService;
    private final BotUserService botUserService;

    @Override
    public void processTextMessage(TextMessage textMessage) {

        String authorChatId = textMessage.getAuthorId();
        String text = textMessage.getText();
        Integer replyToMessageId = textMessage.getReplyToMessageId();
        String receiverChatId = textMessage.getAuthorId();

        BotUser botUser = botUserService.getUser(authorChatId);
        BotUserState userState = botUser.getState();

        if (userState == BotUserState.IN_SEARCH){
            text = "Вы находитесь в поиске. Для остановки используйте /stop";
        }else if(userState == BotUserState.IDLE){
            text = "Вы не в диалоге. Для поиска собеседника используйте /search";
        }
        else{
            receiverChatId = textMessage.getAuthorId();
        }

        AnswerTextMessage answerTextMessage = new AnswerTextMessage(receiverChatId,replyToMessageId, text);
        producerService.produceAnswerMessage(answerTextMessage);
    }

    @Override
    public void processCommandMessage(CommandMessage textMessage) {
        String receiverChatId = textMessage.getAuthorId();
        String command = textMessage.getCommand();
        BotUser botUser = botUserService.getUser(receiverChatId);

        String answer = processCommand(command, botUser);

        if (answer != null){
            AnswerTextMessage answerTextMessage = new AnswerTextMessage(receiverChatId,null, answer);
            producerService.produceAnswerMessage(answerTextMessage);
        }
    }

    @Override
    public void processFileMessage(FileMessage fileMessage) {
        String authorChatId = fileMessage.getAuthorId();
        Integer replyToMessageId = fileMessage.getReplyToMessageId();
        String fileId = fileMessage.getFileId();
        FileMessageTypes fileType = fileMessage.getFileType();
        String receiverChatId = fileMessage.getAuthorId();

        BotUser botUser = botUserService.getUser(authorChatId);
        BotUserState userState = botUser.getState();

        if(userState != BotUserState.IN_CONVERSATION){
            String text = null;
            if (userState == BotUserState.IN_SEARCH){
                text = "Вы находитесь в поиске. Для остановки используйте /stop";
            }else if(userState == BotUserState.IDLE){
                text = "Вы не в диалоге. Для поиска собеседника используйте /search";
            }

            AnswerTextMessage answerTextMessage = new AnswerTextMessage(receiverChatId,replyToMessageId, text);
            producerService.produceAnswerMessage(answerTextMessage);
        }
        else{
            receiverChatId = fileMessage.getAuthorId();

            //TODO проверка подписки...

            AnswerFileMessage answerFileMessage = new AnswerFileMessage(receiverChatId, replyToMessageId, fileId, fileType);
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
        else{
            return "Bot:\n неизвестная команда! Для получения списка комманд введите /help";
        }
    }
}
