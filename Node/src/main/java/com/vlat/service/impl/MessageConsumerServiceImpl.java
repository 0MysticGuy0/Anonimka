package com.vlat.service.impl;

import com.vlat.kafkaMessage.CommandMessage;
import com.vlat.kafkaMessage.FileMessage;
import com.vlat.kafkaMessage.TextMessage;
import com.vlat.service.MessageConsumerService;
import com.vlat.service.MessageProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Service
@RequiredArgsConstructor
@Log4j
public class MessageConsumerServiceImpl implements MessageConsumerService {

    private final MessageProcessorService messageProcessorService;

    @Override
    @KafkaListener(topics = "text-message-topic")
    public void getTextMessage(TextMessage textMessage) {
        messageProcessorService.processTextMessage(textMessage);
        log.debug("-=-=-| Received text-message");
    }

    @Override
    @KafkaListener(topics = "command-message-topic")
    public void getCommandMessage(CommandMessage commandMessage) {
        log.debug(String.format("-=-=-| Received command-message: %s from %s", commandMessage.getCommand(), commandMessage.getAuthorId() ));
        messageProcessorService.processCommandMessage(commandMessage);
    }

    @Override
    @KafkaListener(topics = "file-message-topic")
    public void getFileMessage(FileMessage fileMessage) {
        messageProcessorService.processFileMessage(fileMessage);
        log.debug("-=-=-| Received file-message");
    }

}
