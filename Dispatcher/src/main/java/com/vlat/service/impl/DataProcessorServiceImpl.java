package com.vlat.service.impl;

import com.vlat.bot.service.BotCommandsService;
import com.vlat.kafkaMessage.CommandMessage;
import com.vlat.kafkaMessage.TextMessage;
import com.vlat.service.BotService;
import com.vlat.service.DataProcessorService;
import com.vlat.service.ProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class DataProcessorServiceImpl implements DataProcessorService {

    private final BotService botService;
    private final ProducerService producerService;
    private final BotCommandsService botCommandsService;

    @Override
    public void processUpdate(Update update) {
        if(!update.hasMessage()) return;

        try{
            Message message = update.getMessage();

            boolean processedMessage = false;

            if (message.hasText()){
                processTextMessage(message);
                processedMessage = true;
            }

            if(!processedMessage){
                botService.sendMessage(message.getChatId().toString(),
                        "<!Неподдерживаемый тип сообщения!>", message.getMessageId());
            }
            //Sticker Photo Video Voice VideoNote-fileId


        }catch (Exception ex){
            System.out.println("-=-=-| ERROR IN DataProcessorServiceImpl - processUpdate:\n" + ex);
        }
    }

    private void processTextMessage(Message message){
        String chatId = message.getChatId().toString();
        String messageText = message.getText();
        Integer replyToId = getReplyToMessageId(message);

        if(botCommandsService.isCommand(messageText)){
            processCommand(chatId, messageText);
            return;
        }

        TextMessage textMessage = new TextMessage(messageText, chatId, replyToId);
        producerService.produceTextMessage(textMessage);
    }

    private void processCommand(String chatId, String command){
        CommandMessage commandMessage = new CommandMessage(command, chatId);
        producerService.produceCommandMessage(commandMessage);
    }


    private Integer getReplyToMessageId(Message message){
        Message replyMessage = message.getReplyToMessage();
        if (replyMessage != null){
            return replyMessage.getMessageId();
        }
        return null;
    }
}
