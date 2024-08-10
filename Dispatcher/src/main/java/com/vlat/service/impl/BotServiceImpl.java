package com.vlat.service.impl;

import com.vlat.controller.AnonimkaBot;
import com.vlat.kafkaMessage.AnswerFileMessage;
import com.vlat.kafkaMessage.AnswerMessage;
import com.vlat.kafkaMessage.AnswerTextMessage;
import com.vlat.kafkaMessage.enums.FileMessageTypes;
import com.vlat.service.BotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.vlat.kafkaMessage.enums.FileMessageTypes.*;

@Service
@RequiredArgsConstructor
@Log4j
public class BotServiceImpl implements BotService {

    @Autowired
    private final AnonimkaBot bot;

    @Override
    public void sendMessage(SendMessage message){
        if(message != null){
            try {
                message.setParseMode(ParseMode.MARKDOWN);
                bot.execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void sendMessage(String chatId, String text, Integer replyToId) {
        SendMessage sendMessage = new SendMessage(chatId, text);
        sendMessage.setReplyToMessageId(replyToId);
        sendMessage(sendMessage);
    }

    @Override
    public void sendMessage(AnswerMessage answerMessage) {
        if (answerMessage instanceof AnswerTextMessage){
            sendTextAnswer((AnswerTextMessage) answerMessage);
        }
        else if(answerMessage instanceof AnswerFileMessage){
            sendFileAnswer((AnswerFileMessage) answerMessage);
        }
    }

    private void sendTextAnswer(AnswerTextMessage answerTextMessage){
        String receiverChatId = answerTextMessage.getReceiverChatId();
        Integer replyToMessageId = answerTextMessage.getReplyToMessageId();
        String text = answerTextMessage.getText();

        sendMessage(receiverChatId, text, replyToMessageId);
    }

    private void sendFileAnswer(AnswerFileMessage answerMessage) {
        String fileId = answerMessage.getFileId();
        String receiverChatId = answerMessage.getReceiverChatId();
        Integer replyToMessageId = answerMessage.getReplyToMessageId();
        FileMessageTypes fileType = answerMessage.getFileType();

        InputFile inputFile = getInputFile(fileId);
        executeSendFile(fileType, receiverChatId, inputFile, replyToMessageId);
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

    private void executeSendFile(FileMessageTypes fileType, String receiverChatId, InputFile inputFile, Integer replyToMessageId) {
        try{
            if (fileType == PHOTO){
                SendPhoto sendPhoto = new SendPhoto(receiverChatId, inputFile);
                sendPhoto.setReplyToMessageId(replyToMessageId);
                sendPhoto.setReplyToMessageId(replyToMessageId);
                bot.execute(sendPhoto);
            } else if (fileType == STICKER){
                SendSticker sendSticker = new SendSticker(receiverChatId, inputFile);
                sendSticker.setReplyToMessageId(replyToMessageId);
                bot.execute(sendSticker);
            } else if (fileType == VOICE){
                SendVoice sendVoice = new SendVoice(receiverChatId, inputFile);
                sendVoice.setReplyToMessageId(replyToMessageId);
                bot.execute(sendVoice);
            } else if (fileType == VIDEO){
                SendVideo sendVideo = new SendVideo(receiverChatId, inputFile);
                sendVideo.setReplyToMessageId(replyToMessageId);
                bot.execute(sendVideo);
            } else if (fileType == VIDEO_NOTE){
                SendVideoNote sendVideoNote = new SendVideoNote(receiverChatId, inputFile);
                sendVideoNote.setReplyToMessageId(replyToMessageId);
                bot.execute(sendVideoNote);
            }
        } catch (TelegramApiException e) {
            log.error("-=-=-| Exception while executing send..." + fileType + " :\n" + e.getMessage());
            e.printStackTrace();
        }
    }
}
