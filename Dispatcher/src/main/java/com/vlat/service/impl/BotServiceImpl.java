package com.vlat.service.impl;

import com.vlat.controller.AnonimkaBot;
import com.vlat.kafkaMessage.AnswerFileMessage;
import com.vlat.kafkaMessage.AnswerMessage;
import com.vlat.kafkaMessage.AnswerTextMessage;
import com.vlat.kafkaMessage.enums.FileMessageTypes;
import com.vlat.service.BotService;
import com.vlat.service.MessageLinkerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.vlat.kafkaMessage.enums.FileMessageTypes.*;

@Service
@RequiredArgsConstructor
@Log4j
public class BotServiceImpl implements BotService {

    @Autowired
    private final AnonimkaBot bot;
    @Autowired
    private final MessageLinkerService messageLinkerService;

    @Override
    public Integer sendMessage(SendMessage message){
        if(message != null){
            try {
                message.setParseMode(ParseMode.MARKDOWN);
                Message sendedMessage =  bot.execute(message);
                if(sendedMessage != null){
                    return sendedMessage.getMessageId();
                }
            } catch (TelegramApiException e) {
                log.error("-=-=-| ERROR while execute sendMessage: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Integer sendMessage(String chatId, String text, Integer replyToId) {
        SendMessage sendMessage = new SendMessage(chatId, text);
        sendMessage.setReplyToMessageId(replyToId);
        return sendMessage(sendMessage);
    }

    @Override
    public Integer sendMessage(AnswerMessage answerMessage) {
        if (answerMessage instanceof AnswerTextMessage){
            return sendTextAnswer((AnswerTextMessage) answerMessage);
        }
        else if(answerMessage instanceof AnswerFileMessage){
            return sendFileAnswer((AnswerFileMessage) answerMessage);
        }
        return null;
    }

    private Integer sendTextAnswer(AnswerTextMessage answerTextMessage){
        String receiverChatId = answerTextMessage.getReceiverChatId();
        String senderChatId = answerTextMessage.getSenderChatId();
        String text = answerTextMessage.getText();

        Integer replyToMessageId = answerTextMessage.getReplyToMessageId();
        replyToMessageId = messageLinkerService.getLinkedMessageId(senderChatId, receiverChatId, replyToMessageId);

        Integer sentMessageId = sendMessage(receiverChatId, text, replyToMessageId);

        messageLinkerService.createLink(answerTextMessage, sentMessageId);

        return sentMessageId;
    }

    private Integer sendFileAnswer(AnswerFileMessage answerMessage) {
        String fileId = answerMessage.getFileId();
        String receiverChatId = answerMessage.getReceiverChatId();
        String senderChatId = answerMessage.getSenderChatId();
        FileMessageTypes fileType = answerMessage.getFileType();

        Integer replyToMessageId = answerMessage.getReplyToMessageId();
        replyToMessageId = messageLinkerService.getLinkedMessageId(senderChatId, receiverChatId, replyToMessageId);


        InputFile inputFile = getInputFile(fileId);
        Integer sentMessageId =  executeSendFile(fileType, receiverChatId, inputFile, replyToMessageId);

        messageLinkerService.createLink(answerMessage, sentMessageId);

        return  sentMessageId;
    }

    private InputFile getInputFile(String fileId) {
        GetFile getFile = new GetFile(fileId);

        File tgFile = null;
        java.io.File file = null;
        try {
            tgFile = bot.execute(getFile);
            file = bot.downloadFile(tgFile);
        } catch (TelegramApiException e) {
            log.error("-=-=-| Exception while sendingFileAnswer: getFile / downloadFile:\n" + e.getMessage());
            e.printStackTrace();
        }

        if (file == null){
            log.error("-=-=-| Error while sendingFileAnswer: file=null");
        }

        return new InputFile(file);
    }

    private Integer executeSendFile(FileMessageTypes fileType, String receiverChatId, InputFile inputFile, Integer replyToMessageId) {
        Message sentMessage = null;
        try{
            if (fileType == PHOTO){
                SendPhoto sendPhoto = new SendPhoto(receiverChatId, inputFile);
                sendPhoto.setReplyToMessageId(replyToMessageId);
                sendPhoto.setReplyToMessageId(replyToMessageId);
                sentMessage = bot.execute(sendPhoto);
            } else if (fileType == STICKER){
                SendSticker sendSticker = new SendSticker(receiverChatId, inputFile);
                sendSticker.setReplyToMessageId(replyToMessageId);
                sentMessage = bot.execute(sendSticker);
            } else if (fileType == VOICE){
                SendVoice sendVoice = new SendVoice(receiverChatId, inputFile);
                sendVoice.setReplyToMessageId(replyToMessageId);
                sentMessage = bot.execute(sendVoice);
            } else if (fileType == VIDEO){
                SendVideo sendVideo = new SendVideo(receiverChatId, inputFile);
                sendVideo.setReplyToMessageId(replyToMessageId);
                sentMessage = bot.execute(sendVideo);
            } else if (fileType == VIDEO_NOTE){
                SendVideoNote sendVideoNote = new SendVideoNote(receiverChatId, inputFile);
                sendVideoNote.setReplyToMessageId(replyToMessageId);
                sentMessage = bot.execute(sendVideoNote);
            }
        } catch (TelegramApiException e) {
            log.error("-=-=-| Exception while executing send...(file)" + fileType + " :\n" + e.getMessage());
            e.printStackTrace();
        }

        if(sentMessage != null){
            return sentMessage.getMessageId();
        }
        return null;
    }
}
