package com.vlat.kafkaMessage;

import com.vlat.bot.BotCommands;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchMessage implements Serializable { //сообщение для поиска/остановки собеседника

    private String userChatId;
    private BotCommands command;

}
