package com.vlat.service.impl;

import com.vlat.kafkaMessage.AnswerMessage;
import com.vlat.kafkaMessage.SearchMessage;
import com.vlat.service.ProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProducerServiceImpl implements ProducerService {

    private final KafkaTemplate<String, AnswerMessage> answerMessageKafkaTemplate;

    @Value("${kafka.topics.answer-message.name}")
    private String answerMessageTopic;

    @Override
    public void produceAnswerMessage(AnswerMessage answerMessage) {
        answerMessageKafkaTemplate.send(answerMessageTopic, answerMessage.getReceiverChatId(), answerMessage);
    }

}
