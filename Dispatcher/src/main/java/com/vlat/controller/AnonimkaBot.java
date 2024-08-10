package com.vlat.controller;

import com.vlat.bot.BotCommands;
import com.vlat.service.DataProcessorService;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
@Log4j
public class AnonimkaBot extends TelegramLongPollingBot {

    @Value("${bot.username}")
    private String botUsername;
    @Setter
    private DataProcessorService dataProcessorService;

    @Autowired
    public AnonimkaBot(@Value("${bot.token}") String botToken) {
        super(botToken);
        createCommandsMenu();
        log.debug("-=-=-=| Bot bean created");
    }

    private void createCommandsMenu() {
        List<BotCommand> listOfCommands = new ArrayList<>();
        for(BotCommands cmd:BotCommands.values()){
            listOfCommands.add(new BotCommand(cmd.toString(), cmd.getDescription()));
        }
        SetMyCommands setMyCommands = new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null);
        try {
            execute(setMyCommands);
        } catch (TelegramApiException e) {
            log.error("-=-=-=-=-=-| ERROR while trying to create menu with commands: " + e.getMessage());
        }
    }


    @Override
    public void onUpdateReceived(Update update) {
        try {
            dataProcessorService.processUpdate(update);
        }catch (NullPointerException ex){
            log.error("-=-=-| ERROR IN AnonimkaBot: dataProcessService is null");
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

}
