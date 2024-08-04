package com.vlat.service;

import com.vlat.bot.BotCommands;
import com.vlat.entity.BotUser;

public interface CommandProcessorService {
    String help();
    String start();
    String search(BotUser botUser);
    String stop(BotUser botUser);

    BotCommands parseCommand(String command);

}
