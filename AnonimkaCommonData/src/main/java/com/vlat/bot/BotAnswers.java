package com.vlat.bot;

import static com.vlat.bot.BotCommands.*;

public abstract class BotAnswers {

    public static final String COMPANION_FOUND = String.format(
            "Собеседник найден, общайтесь: \n%s - остановка диалога\n/%s- следующий собеседник", STOP, NEXT);
    public static final String STOPPED_DIALOG = String.format(
            "Диалог остановлен. Для поиска нового собеседника используйте %s", SEARCH);
    public static final String COMPANION_STOPPED_DIALOG = String.format(
            "К сожалению, собеседник остановил диалог. Для поиска нового собеседника используйте %s", SEARCH);
    public static final String STOPPED_SEARCH = String.format(
            "Поиск собеседника остановлен. Для возобновления используйте %s", SEARCH);
    public static final String ALREADY_IN_SEARCH = String.format(
            "Вы уже находитесь в поиске. Для остановки используйте %s", STOP);
    public static final String IN_SEARCH = String.format(
            "Вы находитесь в поиске. Для остановки используйте  %s", STOP);
    public static final String ALREADY_HAS_COMPANION = String.format(
            "У вас уже есть собеседник.\nДля поиска следующего используйте %s\nДля остановки используйте %s", NEXT, STOP);
    public static final String NOT_IN_DIALOG = String.format(
            "Вы не находитесь в диалоге. Для поиска собеседника используйте %s", SEARCH);

    public static final String SEARCHING_NEXT = "Поиск нового собеседника...";
    public static final String UNSUPPORTED_MESSAGE_TYPE = "<!Неподдерживаемый тип сообщения!>";

    public static final String ERROR =  String.format(
            "Ошибка! Для отмены используйте %s", STOP);


    public static final String COMMAND_START = String.format(
            "Вас приветствует Анонимка - телеграм-бот для анонимного общения разных людей.\nСписок команд: %s\n\n%s - запустить поиск", HELP, SEARCH);
    public static final String COMMAND_SEARCH = "Идёт поиск собеседника...";
    public static final String COMMAND_STOP = "Остановка...";
    public static final String COMMAND_NEXT = "Поиск следующего собеседника...";

    public static final String UNKNOWN_COMMAND = String.format(
            "неизвестная команда! Для получения списка команд введите %s", HELP);
}
