package com.vlat.service;

import com.vlat.kafkaMessage.SearchMessage;

public interface SearchMessageProcessorService {
    void processSearchMessage(SearchMessage searchMessage);
}
