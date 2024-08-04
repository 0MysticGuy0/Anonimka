package com.vlat.service.impl;

import com.vlat.kafkaMessage.AnswerFileMessage;
import com.vlat.kafkaMessage.AnswerTextMessage;
import com.vlat.service.AnswerConsumerService;
import com.vlat.service.BotService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@KafkaListener(topics = "answer-message-topic")
@RequiredArgsConstructor
public class AnswerConsumerServiceImpl implements AnswerConsumerService {

    private final BotService botService;

    @Override
    @KafkaHandler
    public void getAnswerTextMessage(AnswerTextMessage answerTextMessage) {
        System.out.println("-=-=-| Received answer-message");
        botService.sendMessage(answerTextMessage);
    }

    @Override
    @KafkaHandler
    public void getAnswerFileMessage(AnswerFileMessage answerFileMessage) {
        System.out.println("-=-=-| Received answer-file-message");
        botService.sendMessage(answerFileMessage);
    }
}
