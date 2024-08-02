package com.vlat.service;

import com.vlat.kafkaMessage.CommandMessage;
import com.vlat.kafkaMessage.FileMessage;
import com.vlat.kafkaMessage.TextMessage;

public interface MessageProcessorService {

    void processTextMessage(TextMessage textMessage);
    void processCommandMessage(CommandMessage textMessage);
    void processFileMessage(FileMessage fileMessage);

}
