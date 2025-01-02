package com.vlat.service;

import com.vlat.entity.BotUser;

public interface AnswerGenerationService {
    void createAnswer(BotUser botUser, String answerText);

    void createAnswer(BotUser botUser, String answerText, boolean clearLinks);

}
