package com.vlat.service.impl;

import com.vlat.entity.BotUser;
import com.vlat.repository.BotUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BotUserServiceImplTest {

    @InjectMocks
    private BotUserServiceImpl botUserService;

    @Mock
    private BotUserRepository botUserRepository;

    @Test
    void testGetNewUser(){
        final String chatId = "abcdefg";

        BotUser newUser = new BotUser(chatId);


        Mockito.when(botUserRepository.findById(chatId)).thenReturn(Optional.empty());
        Mockito.when(botUserRepository.save(Mockito.any())).thenReturn(newUser);

        BotUser savedUser = botUserService.getUser(chatId);

        Assertions.assertEquals(newUser, savedUser);
        Mockito.verify(botUserRepository).save(Mockito.any());
    }

    @Test
    void testGetExistingUser(){
        final String chatId = "abcdefg";
        BotUser existingUser = new BotUser(chatId);

        Mockito.when(botUserRepository.findById(chatId)).thenReturn(Optional.of(existingUser));

        BotUser foundUser = botUserService.getUser(chatId);
        Assertions.assertEquals(existingUser, foundUser);
        Mockito.verify(botUserRepository).findById(chatId);
        Mockito.verify(botUserRepository, Mockito.never()).save(Mockito.any()); // проверка, что ничего не сохраняется
    }

    @Test
    void testSaveUser(){
        final String chatId = "abcdefg";
        BotUser user = new BotUser(chatId);

        Mockito.when(botUserRepository.save(user)).thenReturn(user);

        BotUser savedUser = botUserService.saveUser(user);

        Assertions.assertEquals(user, savedUser);
        Mockito.verify(botUserRepository).save(user);
    }

    @Test
    void testGetAllUsersInSearch(){
        BotUser[] bu = {new BotUser("1"), new BotUser("2"), new BotUser("3")};
        List<BotUser> usersInSearch = List.of(bu);

        Mockito.when(botUserRepository.findAllUsersInSearch()).thenReturn(usersInSearch);

        List<BotUser> resultList = botUserService.getAllUsersInSearch();

        Assertions.assertEquals(usersInSearch, resultList);
        Mockito.verify(botUserRepository).findAllUsersInSearch();
    }
}