package com.vlat.configuration;

import com.vlat.bot.service.BotCommandsService;
import com.vlat.bot.service.impl.BotCommandsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class NodeConfiguration {

    @Bean
    public BotCommandsService botCommandsService(){
        return new BotCommandsServiceImpl();
    }
}
