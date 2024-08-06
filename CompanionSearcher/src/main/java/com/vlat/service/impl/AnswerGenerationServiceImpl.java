package com.vlat.service.impl;

import com.vlat.entity.BotUser;
import com.vlat.kafkaMessage.AnswerTextMessage;
import com.vlat.service.AnswerGenerationService;
import com.vlat.service.ProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AnswerGenerationServiceImpl implements AnswerGenerationService {

    private final ProducerService producerService;

    @Override
    public void createAnswer(BotUser botUser, String answerText) {
        String receiverChatId = botUser.getChatId();
        answerText = "*" + answerText + "*";
        AnswerTextMessage answerTextMessage = new AnswerTextMessage(receiverChatId,null, answerText);

        producerService.produceAnswerMessage(answerTextMessage);
    }
}
