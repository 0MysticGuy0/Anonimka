package com.vlat.service;

import com.vlat.entity.BotUser;

public interface SearcherService {
    void stop(BotUser botUser);
    void search(BotUser botUser);
    void next(BotUser botUser);
}
