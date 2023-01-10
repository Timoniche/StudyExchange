package com.studyexchange.telegrambot.stateactions;

import com.pengrad.telegrambot.TelegramBot;
import com.studyexchange.core.UserState;
import com.studyexchange.service.UserService;

import java.util.Map;

public class StateActionFactory {
    private final Map<UserState, BaseStateAction> stateToAction;

    public StateActionFactory(TelegramBot bot, UserService userService) {
        stateToAction = Map.of(
                UserState.NO_NAME_INTRO, new NoNameIntroAction(bot, userService),
                UserState.REQUEST_HELP_EDUCATIONAL, new RequestHelpEducational(bot, userService)
        );
    }

    public BaseStateAction stateActionFrom(UserState userState) {
        return switch (userState) {
            case NO_NAME_INTRO -> stateToAction.get(UserState.NO_NAME_INTRO);
            case REQUEST_HELP_EDUCATIONAL -> stateToAction.get(UserState.REQUEST_HELP_EDUCATIONAL);
        };
    }
}
