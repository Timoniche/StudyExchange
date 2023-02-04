package com.studyexchange.telegrambot.stateactions;

import com.pengrad.telegrambot.model.Update;
import com.studyexchange.core.UserState;

import java.util.Optional;

public interface StateAction {

    /**
     * @return date question is delivered
     */
    int setupStateAndAskQuestions(long chatId);

    /**
     * @return empty optional if processing update was not successful, next UserState to setup otherwise
     */
    Optional<UserState> processAnswerAndReturnNextStateToSetup(Update update);
}

