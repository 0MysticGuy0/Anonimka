package com.vlat.service.impl;

import com.vlat.controller.AnonimkaBot;
import com.vlat.kafkaMessage.AnswerMessage;
import com.vlat.kafkaMessage.AnswerTextMessage;
import com.vlat.service.BotService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
public class BotServiceImpl implements BotService {

    @Autowired
    private final AnonimkaBot bot;

    @Override
    public void sendMessage(SendMessage message){
        if(message != null){
            try {
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
    }

    private void sendTextAnswer(AnswerTextMessage answerTextMessage){
        String receiverChatId = answerTextMessage.getReceiverChatId();
        Integer replyToMessageId = answerTextMessage.getReplyToMessageId();
        String text = answerTextMessage.getText();

        sendMessage(receiverChatId, text, replyToMessageId);
    }
}
