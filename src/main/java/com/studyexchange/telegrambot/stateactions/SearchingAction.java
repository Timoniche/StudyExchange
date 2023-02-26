package com.studyexchange.telegrambot.stateactions;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.studyexchange.core.UserState;
import com.studyexchange.service.UserService;

import java.util.Optional;

public class SearchingAction extends BaseStateAction {
    public SearchingAction(TelegramBot bot, UserService userService) {
        super(bot, userService);
    }

    @Override
    public int setupStateAndAskQuestions(long chatId) {
        return 0;
    }

    @Override
    public Optional<UserState> processAnswerAndReturnNextStateToSetup(Update update) {
        return Optional.empty();
    }
}
