package com.vlat.service;

import com.vlat.kafkaMessage.AnswerMessage;
import com.vlat.kafkaMessage.CommandMessage;
import com.vlat.kafkaMessage.SearchMessage;
import com.vlat.kafkaMessage.TextMessage;

public interface ProducerService {
    void produceAnswerMessage(AnswerMessage answerMessage);
    void produceSearchMessage(SearchMessage searchMessage);
}
