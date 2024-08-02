package com.vlat.service;

import com.vlat.kafkaMessage.CommandMessage;
import com.vlat.kafkaMessage.TextMessage;

public interface MessageProcessorService {

    void processTextMessage(TextMessage textMessage);
    void processCommandMessage(CommandMessage textMessage);

}
