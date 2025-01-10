package com.vlat.service.impl;

import com.vlat.bot.service.BotCommandsService;
import com.vlat.kafkaMessage.CommandMessage;
import com.vlat.kafkaMessage.FileMessage;
import com.vlat.kafkaMessage.TextMessage;
import com.vlat.kafkaMessage.enums.FileMessageTypes;
import com.vlat.service.BotService;
import com.vlat.service.DataProcessorService;
import com.vlat.service.MessageLinkerService;
import com.vlat.service.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.vlat.kafkaMessage.enums.FileMessageTypes.*;

@Service
@RequiredArgsConstructor
@Log4j
public class DataProcessorServiceImpl implements DataProcessorService {

    private final BotService botService;
    private final ProducerService producerService;
    private final BotCommandsService botCommandsService;
    private final MessageLinkerService messageLinkerService;

    @Override
    public void processUpdate(Update update) {
        //if(!update.hasMessage()) return;

        if(update.hasMessage()){
            Message message = update.getMessage();
            processMessage(message);
        }
        if(update.hasEditedMessage()){
           Message message = update.getEditedMessage();
           processEditMessage(message);
        }

    }

private void processMessage(Message message){
    boolean processedMessage = false;

    if (message.hasText()){
        processTextMessage(message);
        processedMessage = true;
    }

    if(message.hasPhoto()){
        processFileMessage(message, PHOTO);
        //botService.testPhoto(fileId, message.getChatId());
        processedMessage = true;
    }
    if(message.hasSticker()){
        processFileMessage(message, STICKER);
        processedMessage = true;
    }
    if(message.hasVoice()){
        processFileMessage(message, VOICE);
        processedMessage = true;
    }
    if(message.hasVideo()){
        processFileMessage(message, VIDEO);
        processedMessage = true;
    }
    if(message.hasVideoNote()){
        processFileMessage(message, VIDEO_NOTE);
        processedMessage = true;
    }

    if(!processedMessage){
        botService.sendMessage(message.getChatId().toString(),
                "<!Неподдерживаемый тип сообщения!>", message.getMessageId());
    }
    //Sticker Photo Video Voice VideoNote-fileId
}

    private void processTextMessage(Message message){
        String chatId = message.getChatId().toString();
        String messageText = message.getText();
        Integer messageId = message.getMessageId();
        Integer replyToId = getReplyToMessageId(message);

        log.debug(String.format("Received MESSAGE: \n\t %s |-|-| BY %s", messageText, message.getFrom()));

        if(botCommandsService.isCommand(messageText)){
            processCommand(chatId, messageText);
            return;
        }

        TextMessage textMessage = new TextMessage(chatId, replyToId, messageId, messageText);
        producerService.produceTextMessage(textMessage);
    }

    private void processCommand(String chatId, String command){
        CommandMessage commandMessage = new CommandMessage(chatId, command);
        producerService.produceCommandMessage(commandMessage);
    }

    private void processFileMessage(Message message, FileMessageTypes fileType){
        FileMessage fileMessage = getFileMessageFromMessage(message,fileType);
        producerService.produceFileMessage(fileMessage);
    }

    private FileMessage getFileMessageFromMessage(Message message, FileMessageTypes fileType){
        String fileId = getFileId(message, fileType);
        String chatId = message.getChatId().toString();
        Integer messageId = message.getMessageId();
        Integer replyToId = getReplyToMessageId(message);

        return new FileMessage(chatId, replyToId, messageId, fileId, fileType);
    }

    private String getFileId(Message message, FileMessageTypes fileType){
        if(fileType == PHOTO){
            int photoSizesNum = message.getPhoto().size();
            PhotoSize photo = message.getPhoto().get(photoSizesNum - 1);
            return photo.getFileId();
        }else if(fileType == FileMessageTypes.STICKER){
             return message.getSticker().getFileId();
        }else if(fileType == FileMessageTypes.VOICE){
            return message.getVoice().getFileId();
        }else if(fileType == FileMessageTypes.VIDEO){
            return message.getVideo().getFileId();
        }else if(fileType == FileMessageTypes.VIDEO_NOTE){
            return message.getVideoNote().getFileId();
        }
        else{
            log.error(String.format("-=-=-=-| UNKNOWN FILE TYPE RECEIVED FROM USER AND STARTED PROCESSING: %s", fileType));
            return null;
        }
    }


    private Integer getReplyToMessageId(Message message){
        Message replyMessage = message.getReplyToMessage();
        if (replyMessage != null){
            return replyMessage.getMessageId();
        }
        return null;
    }

    private void processEditMessage(Message message){
        Long chatId = message.getChatId();
        Integer messageId = message.getMessageId();
        String text = message.getText();

        String[] linkedData = messageLinkerService.getLinkedData(chatId.toString(), messageId);

        if(linkedData != null && linkedData.length == 2){
            String dataChatId = linkedData[0];
            try{
                Integer dataMessageId = Integer.parseInt(linkedData[1]);
                botService.editMessageText(dataChatId, dataMessageId, text);
            } catch (NumberFormatException e) {
                log.error(String.format("-=-=-=-| Didn't edited message at companion because couldn't parse messageId: %s", linkedData));

            }
        }
    }
}
