package com.vlat.service;

import com.vlat.kafkaMessage.AnswerMessage;

public interface MessageLinkerService {
    void createLink(AnswerMessage answerMessage, Integer sentMessageId);
    Integer getLinkedMessageId(String senderChatId, String receiverChatId, Integer messageId);
    String[] getLinkedData(String senderChatId, Integer messageId);

    void clearUserLinks(String userChatId);
}
