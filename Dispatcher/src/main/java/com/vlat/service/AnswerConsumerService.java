package com.vlat.service;

import com.vlat.kafkaMessage.AnswerFileMessage;
import com.vlat.kafkaMessage.AnswerTextMessage;

public interface AnswerConsumerService {
    void getAnswerTextMessage(AnswerTextMessage answerTextMessage);
    void getAnswerFileMessage(AnswerFileMessage answerFileMessage);
}
