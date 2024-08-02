package com.vlat.service;

import com.vlat.kafkaMessage.CommandMessage;
import com.vlat.kafkaMessage.FileMessage;
import com.vlat.kafkaMessage.TextMessage;

public interface ProducerService {
    void produceTextMessage(TextMessage textMessage);
    void produceCommandMessage(CommandMessage commandMessage);
    void produceFileMessage(FileMessage fileMessage);
}
