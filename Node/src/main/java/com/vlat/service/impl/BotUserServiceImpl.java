package com.vlat.service.impl;

import com.vlat.entity.BotUser;
import com.vlat.repository.BotUserRepository;
import com.vlat.service.BotUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BotUserServiceImpl implements BotUserService {

    private final BotUserRepository botUserRepository;

    @Override
    public BotUser getUser(String chatId) {
        Optional<BotUser> optionalBotUser = botUserRepository.findById(chatId);
        if(optionalBotUser.isPresent()){
            return optionalBotUser.get();
        }

        BotUser newBotUser = botUserRepository.save(new BotUser(chatId));
        return newBotUser;
    }

    @Override
    public BotUser saveUser(BotUser botUser) {
        BotUser persistentBotUser = botUserRepository.save(botUser);
        return persistentBotUser;
    }
}
