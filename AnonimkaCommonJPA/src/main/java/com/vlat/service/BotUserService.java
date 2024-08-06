package com.vlat.service;

import com.vlat.entity.BotUser;

public interface BotUserService {

    BotUser getUser(String chatId);
    BotUser saveUser(BotUser botUser);

}
