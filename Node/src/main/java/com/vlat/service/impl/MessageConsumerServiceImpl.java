package com.vlat.service.impl;

import com.vlat.kafkaMessage.CommandMessage;
import com.vlat.kafkaMessage.TextMessage;
import com.vlat.service.MessageConsumerService;
import com.vlat.service.MessageProcessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class MessageConsumerServiceImpl implements MessageConsumerService {

    private final MessageProcessorService messageProcessorService;

    @Override
    @KafkaListener(topics = "text-message-topic")
    public void getTextMessage(TextMessage textMessage) {
        messageProcessorService.processTextMessage(textMessage);
        System.out.println("-=-=-| Received text-message");
    }

    @Override
    @KafkaListener(topics = "command-message-topic")
    public void getCommandMessage(CommandMessage commandMessage) {
        messageProcessorService.processCommandMessage(commandMessage);
        System.out.println("-=-=-| Received command-message");
    }

}
