package com.vlat.configuration;

import com.vlat.bot.service.BotCommandsService;
import com.vlat.bot.service.impl.BotCommandsServiceImpl;
import com.vlat.controller.AnonimkaBot;
import com.vlat.service.DataProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


@Configuration
public class BotConfiguration {


    @Bean
    @Autowired
    public TelegramBotsApi telegramBotsApi(AnonimkaBot bot, DataProcessorService dataProcessorService) throws TelegramApiException {
        bot.setDataProcessorService(dataProcessorService);
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(bot);



        System.out.println("-=-=-=| TelegramBotsApi bean created");
        return api;
    }

    @Bean
    public BotCommandsService botCommandsService(){
        return new BotCommandsServiceImpl();
    }

}
