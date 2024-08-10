package com.vlat.service.impl;

import com.vlat.kafkaMessage.CommandMessage;
import com.vlat.kafkaMessage.FileMessage;
import com.vlat.kafkaMessage.SearchMessage;
import com.vlat.kafkaMessage.TextMessage;
import com.vlat.service.MessageConsumerService;
import com.vlat.service.SearchMessageProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
@Log4j
public class MessageConsumerServiceImpl implements MessageConsumerService {

    private final SearchMessageProcessorService searchMessageProcessorService;

    @Override
    @KafkaListener(topics = "search-message-topic")
    public void getSearchMessage(SearchMessage searchMessage) {
        log.debug("-=-=| Received search-message: " + searchMessage.getCommand() + " from " + searchMessage.getUserChatId());
        searchMessageProcessorService.processSearchMessage(searchMessage);
    }
}
