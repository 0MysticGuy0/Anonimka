package com.vlat.service.impl;

import com.vlat.entity.BotUser;
import com.vlat.entity.enums.BotUserState;
import com.vlat.service.BotUserService;
import com.vlat.service.AnswerGenerationService;
import com.vlat.service.SearcherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearcherServiceImpl implements SearcherService {

    static final List<BotUser> usersInSearch;
    private final BotUserService botUserService;
    private final AnswerGenerationService answerGenerationService;

    static{
        usersInSearch = new ArrayList<>();
    }

    @Override
    public void stop(BotUser botUser) {
        BotUserState state = botUser.getState();

        if (state == BotUserState.IN_CONVERSATION){
            stopDialog(botUser);
        }
        else if(state == BotUserState.IN_SEARCH){
            stopSearch(botUser);
        }
    }

    @Override
    public void search(BotUser botUser) {
        if (botUser.getState() == BotUserState.IDLE){
            BotUser companion = findCompanionFor(botUser);
            if(companion == null){
                addUserToQueue(botUser);
            }
            else{
                createDialog(botUser, companion);
            }
        }
        else{
            System.out.println("-=-=-=-=-=--=-=-| ERROR! DONT SEARCH companion because users state is not IDLE");
        }
    }

    @Override
    public void next(BotUser botUser) {
        if(botUser.getState() == BotUserState.IN_CONVERSATION){
            BotUser companion = botUser.getCompanion();
            if (companion != null){
                scipCompanion(botUser);
            }else{
                System.out.println("-=-=-=-=-=-=-=-| ERROR! state is IN_CONVERSATION, but companion = null for " + botUser);
            }
        }
        else{
            System.out.println("-=-=-=-=-=--=-=-| ERROR! Don't do NEXT companion because users state is not IN_CONVERSATION");
        }
    }

    private static BotUser findCompanionFor(BotUser botUser){
        for(BotUser companion:usersInSearch){
            //TODO проверки фильтров
            return companion;
        }
        return null;
    }

    private void addUserToQueue(BotUser botUser){
        botUser.setState(BotUserState.IN_SEARCH);
        botUserService.saveUser(botUser);
        usersInSearch.add(botUser);
        System.out.println("-=-=-| User " + botUser.getChatId() + " added to inSearch list");
        System.out.println("CURRENT LIST: ");
        usersInSearch.forEach(System.out::println);
    }

    private void createDialog(BotUser botUser, BotUser companion){
        usersInSearch.remove(companion);

        botUser.setState(BotUserState.IN_CONVERSATION);
        companion.setState(BotUserState.IN_CONVERSATION);
        botUser.setCompanion(companion);
        companion.setCompanion(botUser);
        botUserService.saveUser(botUser);
        botUserService.saveUser(companion);

        String foundMessage = "Собеседник найден, общайтесь: \n/stop - остановка диалога\n/next - следующий собеседник";
        answerGenerationService.createAnswer(botUser, foundMessage);
        answerGenerationService.createAnswer(companion, foundMessage);

        System.out.println("-=-=-| CREATED DIALOG FOR: " + botUser.getChatId() + " & " + companion.getChatId());
        System.out.println("CURRENT LIST: ");
        usersInSearch.forEach(System.out::println);

    }

    private void stopDialog(BotUser botUser){
        BotUser companion = botUser.getCompanion();

        botUser.setState(BotUserState.IDLE);
        companion.setState(BotUserState.IDLE);
        botUser.setCompanion(null);
        companion.setCompanion(null);
        botUserService.saveUser(botUser);
        botUserService.saveUser(companion);

        answerGenerationService.createAnswer(botUser, "Диалог остановлен. Для поиска нового собеседника используйте /search");
        answerGenerationService.createAnswer(companion, "Собеседник остановил дилог. Для поиска нового собеседника используйте /search");

        System.out.println("-=-=-| User "+ botUser.getChatId() + " stopped dialog with " + companion.getChatId());
    }

    private void stopSearch(BotUser botUser){
        boolean removeRes = usersInSearch.remove(botUser);

        System.out.println("removed successfully: " + removeRes);

        botUser.setState(BotUserState.IDLE);
        botUserService.saveUser(botUser);

        answerGenerationService.createAnswer(botUser, "Поиск собеседника остановлен. Для возобновления испольуйте /search");

        System.out.println("-=-=-| User " + botUser.getChatId() + " stopped search");
        System.out.println("CURRENT LIST: ");
        usersInSearch.forEach(System.out::println);
    }

    private void scipCompanion(BotUser botUser){
        BotUser companion = botUser.getCompanion();

        companion.setState(BotUserState.IDLE);
        companion.setCompanion(null);
        botUserService.saveUser(companion);
        answerGenerationService.createAnswer(companion, "К сожалению, собеседник остановил диалог. Для поиска нового собеседника используйте /search");

        botUser.setCompanion(null);
        botUser.setState(BotUserState.IDLE);
        answerGenerationService.createAnswer(botUser, "Поиск нового собеседника...");
        search(botUser);
    }
}
