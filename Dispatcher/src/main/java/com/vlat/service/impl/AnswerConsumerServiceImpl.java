package com.vlat.service.impl;

import com.vlat.kafkaMessage.AnswerFileMessage;
import com.vlat.kafkaMessage.AnswerTextMessage;
import com.vlat.service.AnswerConsumerService;
import com.vlat.service.BotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@KafkaListener(topics = "answer-message-topic")
@RequiredArgsConstructor
@Log4j
public class AnswerConsumerServiceImpl implements AnswerConsumerService {

    private final BotService botService;

    @Override
    @KafkaHandler
    public void getAnswerTextMessage(AnswerTextMessage answerTextMessage) {
        log.debug("-=-=-| Received answer-message for " + answerTextMessage.getReceiverChatId());
        botService.sendMessage(answerTextMessage);
    }

    @Override
    @KafkaHandler
    public void getAnswerFileMessage(AnswerFileMessage answerFileMessage) {
        log.debug("-=-=-| Received answer-file-message for " + answerFileMessage.getReceiverChatId());
        botService.sendMessage(answerFileMessage);
    }
}
