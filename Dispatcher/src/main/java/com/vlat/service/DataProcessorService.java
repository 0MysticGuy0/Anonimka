package com.vlat.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface DataProcessorService {

    void processUpdate(Update update);

}
