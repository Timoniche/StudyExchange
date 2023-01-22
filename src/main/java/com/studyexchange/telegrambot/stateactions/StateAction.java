package com.studyexchange.telegrambot.stateactions;

import com.pengrad.telegrambot.model.Update;
import com.studyexchange.core.UserState;

public interface StateAction {

    void setupStateAndAskQuestions(long chatId);

    /**
     * @return NULL if processing update was not successful, next UserState to setup otherwise
     */
    UserState processAnswerAndReturnNextStateToSetup(Update update);
}

