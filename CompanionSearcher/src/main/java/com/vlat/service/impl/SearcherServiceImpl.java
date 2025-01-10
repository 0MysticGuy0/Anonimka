package com.vlat.service.impl;

import com.vlat.entity.BotUser;
import com.vlat.entity.enums.BotUserState;
import com.vlat.service.BotUserService;
import com.vlat.service.AnswerGenerationService;
import com.vlat.service.SearcherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j
public class SearcherServiceImpl implements SearcherService {

    static List<BotUser> usersInSearch;
    private final BotUserService botUserService;
    private final AnswerGenerationService answerGenerationService;

    static{
        usersInSearch = new ArrayList<>();
    }

    public SearcherServiceImpl(BotUserService botUserService, AnswerGenerationService answerGenerationService) {
        this.botUserService = botUserService;
        this.answerGenerationService = answerGenerationService;


        List<BotUser> searchingUsersFromDB = botUserService.getAllUsersInSearch();
        StringBuilder importedUsers = new StringBuilder();
        for(BotUser user: searchingUsersFromDB){
            importedUsers.append("\t").append(user).append("\n");
            user.setState(BotUserState.IDLE);
            search(user);
        }

        log.debug(String.format("Imported saved users IN_SEARCH from DB and restarted search for them because of service restart:\n%s", importedUsers));

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
            log.error(String.format("-=-=-=-=-=--=-=-| ERROR! DONT SEARCH companion because users state is not IDLE: %s", botUser));
        }
    }

    @Override
    public void next(BotUser botUser) {
        if(botUser.getState() == BotUserState.IN_CONVERSATION){
            BotUser companion = botUser.getCompanion();
            if (companion != null){
                scipCompanion(botUser);
            }else{
                log.error(String.format("-=-=-=-=-=-=-=-| ERROR! state is IN_CONVERSATION, but companion = null for %s", botUser));
            }
        }
        else{
            log.error(String.format("-=-=-=-=-=--=-=-| ERROR! Don't do NEXT companion because users state is not IN_CONVERSATION: %s", botUser));
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

        log.debug(String.format("-=-=-| User %s added to inSearch list", botUser.getChatId() ));
        logCurrentInSearchList();
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

        log.debug(String.format("-=-=-| CREATED DIALOG FOR: %s AND %s", botUser.getChatId(), companion.getChatId()));
        logCurrentInSearchList();
    }


    private void stopDialog(BotUser botUser){
        BotUser companion = botUser.getCompanion();

        botUser.setState(BotUserState.IDLE);
        companion.setState(BotUserState.IDLE);
        botUser.setCompanion(null);
        companion.setCompanion(null);
        botUserService.saveUser(botUser);
        botUserService.saveUser(companion);

        answerGenerationService.createAnswer(botUser, "Диалог остановлен. Для поиска нового собеседника используйте /search", true);
        answerGenerationService.createAnswer(companion, "Собеседник остановил дилог. Для поиска нового собеседника используйте /search", true);

        log.debug(String.format("-=-=-| User %s stopped dialog with %s", botUser.getChatId(), companion.getChatId() ));
    }

    private void stopSearch(BotUser botUser){
        boolean removeRes = usersInSearch.remove(botUser);

        log.debug(String.format("%s removed successfully from searchList: %b", botUser.getChatId(), removeRes));

        botUser.setState(BotUserState.IDLE);
        botUserService.saveUser(botUser);

        answerGenerationService.createAnswer(botUser, "Поиск собеседника остановлен. Для возобновления используйте /search");

        log.debug(String.format("-=-=-| User %s stopped search", botUser.getChatId() ));
        logCurrentInSearchList();
    }

    private void scipCompanion(BotUser botUser){
        BotUser companion = botUser.getCompanion();

        companion.setState(BotUserState.IDLE);
        companion.setCompanion(null);
        botUserService.saveUser(companion);
        answerGenerationService.createAnswer(companion, "К сожалению, собеседник остановил диалог. Для поиска нового собеседника используйте /search", true);

        botUser.setCompanion(null);
        botUser.setState(BotUserState.IDLE);
        answerGenerationService.createAnswer(botUser, "Поиск нового собеседника...", true);
        search(botUser);
    }

    private void logCurrentInSearchList(){
        StringBuilder listStr = new StringBuilder();
        listStr.append("| CURRENT LIST: |\n");
        usersInSearch.forEach(usr -> listStr.append("\t").append(usr));
        listStr.append("\n");
        log.debug(listStr);
    }
}
