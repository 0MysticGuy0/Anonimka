package com.vlat.service;

import com.vlat.entity.BotUser;

import java.util.List;

public interface BotUserService {

    BotUser getUser(String chatId);
    BotUser saveUser(BotUser botUser);
    List<BotUser> getAllUsersInSearch();

}
