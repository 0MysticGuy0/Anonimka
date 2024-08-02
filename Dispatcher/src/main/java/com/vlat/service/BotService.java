package com.vlat.service;


import com.vlat.kafkaMessage.AnswerMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface BotService {

    void sendMessage(SendMessage message);
    void sendMessage(String chatId, String text, Integer replyToId);
    void sendMessage(AnswerMessage answerMessage);

}
