package com.vlat.service.impl;

import com.vlat.entity.BotUser;
import com.vlat.repository.BotUserRepository;
import com.vlat.service.BotUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
        System.out.println("\n-=-=-=-=-=-| NEW USER! |-=-=-=-=-=-\n" + newBotUser + "\n");
        return newBotUser;
    }

    @Override
    public BotUser saveUser(BotUser botUser) {
        BotUser persistentBotUser = botUserRepository.save(botUser);
        return persistentBotUser;
    }

    @Override
    public List<BotUser> getAllUsersInSearch() {
        List<BotUser> usersInSearch = botUserRepository.findAllUsersInSearch();

//        System.out.println("-=-=-=-=-=-!-=-=-=-=-=-=-");
//        usersInSearch.forEach(System.out::println);
//        System.out.println("-=-=-=-=-=-!-=-=-=-=-=-=-");

        return usersInSearch;
    }
}
