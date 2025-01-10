package com.vlat.service.impl;

import com.vlat.kafkaMessage.CommandMessage;
import com.vlat.kafkaMessage.FileMessage;
import com.vlat.kafkaMessage.TextMessage;
import com.vlat.service.ProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProducerServiceImpl implements ProducerService {

    private final KafkaTemplate<String, TextMessage> textMessageKafkaTemplate;
    private final KafkaTemplate<String, CommandMessage> commandMessageKafkaTemplate;
    private final KafkaTemplate<String, FileMessage> fileMessageKafkaTemplate;

    @Value("${kafka.topics.text-message.name}")
    private String textMessageTopic;
    @Value("${kafka.topics.command-message.name}")
    private String commandMessageTopic;
    @Value("${kafka.topics.file-message.name}")
    private String fileMessageTopic;

    @Override
    public synchronized void produceTextMessage(TextMessage textMessage) {
        textMessageKafkaTemplate.send(textMessageTopic, textMessage.getAuthorId(), textMessage);

    }

    @Override
    public synchronized void produceCommandMessage(CommandMessage commandMessage) {
        commandMessageKafkaTemplate.send(commandMessageTopic, commandMessage.getAuthorId(), commandMessage);
    }

    @Override
    public synchronized void produceFileMessage(FileMessage fileMessage) {
        fileMessageKafkaTemplate.send(fileMessageTopic, fileMessage.getAuthorId(), fileMessage);
    }
}
