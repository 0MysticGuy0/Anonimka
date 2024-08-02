package com.vlat.service;

import com.vlat.kafkaMessage.CommandMessage;
import com.vlat.kafkaMessage.FileMessage;
import com.vlat.kafkaMessage.TextMessage;

public interface MessageConsumerService {
    void getTextMessage(TextMessage textMessage);
    void getCommandMessage(CommandMessage commandMessage);
    void getFileMessage(FileMessage fileMessage);
}
