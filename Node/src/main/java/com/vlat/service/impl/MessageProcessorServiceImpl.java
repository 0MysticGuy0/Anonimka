package com.vlat.service.impl;

import com.vlat.bot.BotCommands;
import com.vlat.bot.service.BotCommandsService;
import com.vlat.kafkaMessage.*;
import com.vlat.kafkaMessage.enums.FileMessageTypes;
import com.vlat.service.MessageProcessorService;
import com.vlat.service.ProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.vlat.bot.BotCommands.*;

@Service
@RequiredArgsConstructor
public class MessageProcessorServiceImpl implements MessageProcessorService {

    private  final ProducerService producerService;
    private final BotCommandsService botCommandsService;

    @Override
    public void processTextMessage(TextMessage textMessage) {

        String receiverChatId = textMessage.getAuthorId();
        String text = textMessage.getText();
        Integer replyToId = textMessage.getReplyToMessageId();

        AnswerTextMessage answerTextMessage = new AnswerTextMessage(receiverChatId,replyToId, text);
        producerService.produceAnswerMessage(answerTextMessage);
    }

    @Override
    public void processCommandMessage(CommandMessage textMessage) {
        String receiverChatId = textMessage.getAuthorId();
        String command = textMessage.getCommand();

        String answer = processCommand(command);

        AnswerTextMessage answerTextMessage = new AnswerTextMessage(receiverChatId,null, answer);
        producerService.produceAnswerMessage(answerTextMessage);
    }

    @Override
    public void processFileMessage(FileMessage fileMessage) {
        String receiverChatId = fileMessage.getAuthorId();
        Integer replyToId = fileMessage.getReplyToMessageId();
        String fileId = fileMessage.getFileId();
        FileMessageTypes fileType = fileMessage.getFileType();

        //TODO проверка подписки...

        AnswerFileMessage answerFileMessage = new AnswerFileMessage(receiverChatId, replyToId, fileId, fileType);
        producerService.produceAnswerMessage(answerFileMessage);
    }


    private String processCommand(String textCommand){

        BotCommands command = botCommandsService.getBotCommand(textCommand);

        if( command == START){
            return "Вас приветствует Анонимка - телеграм-бот для анонимного общения разных людей.\nСписок команд: /help";
        }
        else if(command == HELP){
            return botCommandsService.getAllCommandsInfo();
        }
        else{
            return "Bot:\n неизвестная команда! Для получения списка комманд введите /help";
        }
    }
}
