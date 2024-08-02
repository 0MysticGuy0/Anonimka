package com.vlat.controller;

import com.vlat.service.DataProcessorService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

@Component
public class AnonimkaBot extends TelegramLongPollingBot {

    @Value("${bot.username}")
    private String botUsername;
    @Setter
    private DataProcessorService dataProcessorService;

    @Autowired
    public AnonimkaBot(@Value("${bot.token}") String botToken) {
        super(botToken);
        System.out.println("-=-=-=| Bot bean created");
    }


    @Override
    public void onUpdateReceived(Update update) {
        try {
            dataProcessorService.processUpdate(update);
        }catch (NullPointerException ex){
            System.out.println("-=-=-| ERROR IN AnonimkaBot: dataProcessService is null");
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

}
