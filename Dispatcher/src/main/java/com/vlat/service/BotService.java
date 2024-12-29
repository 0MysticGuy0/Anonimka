package com.vlat.service;


import com.vlat.kafkaMessage.AnswerFileMessage;
import com.vlat.kafkaMessage.AnswerMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface BotService {

    Integer sendMessage(SendMessage message);
    Integer sendMessage(String chatId, String text, Integer replyToId);
    Integer sendMessage(AnswerMessage answerMessage);
}
