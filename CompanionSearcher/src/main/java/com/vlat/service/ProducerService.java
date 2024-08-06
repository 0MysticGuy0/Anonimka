package com.vlat.service;

import com.vlat.kafkaMessage.AnswerMessage;
import com.vlat.kafkaMessage.SearchMessage;

public interface ProducerService {
    void produceAnswerMessage(AnswerMessage answerMessage);
}
