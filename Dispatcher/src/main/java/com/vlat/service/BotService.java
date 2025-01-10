package com.vlat.service;


import com.vlat.kafkaMessage.AnswerFileMessage;
import com.vlat.kafkaMessage.AnswerMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.concurrent.Future;

public interface BotService {

    Integer sendMessage(SendMessage message);
    Integer sendMessage(String chatId, String text, Integer replyToId);
    void sendMessage(AnswerMessage answerMessage);
    void editMessageText(String chatId, Integer messageId, String newText);
}
