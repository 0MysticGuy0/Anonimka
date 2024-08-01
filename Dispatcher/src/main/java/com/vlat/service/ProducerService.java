package com.vlat.service;

import com.vlat.kafkaMessage.CommandMessage;
import com.vlat.kafkaMessage.TextMessage;

public interface ProducerService {
    void produceTextMessage(TextMessage textMessage);
    void produceCommandMessage(CommandMessage commandMessage);
}
