package com.vlat.service;

import com.vlat.kafkaMessage.CommandMessage;
import com.vlat.kafkaMessage.FileMessage;
import com.vlat.kafkaMessage.SearchMessage;
import com.vlat.kafkaMessage.TextMessage;

public interface MessageConsumerService {
    void getSearchMessage(SearchMessage searchMessage);
}
