package com.studyexchange.telegrambot.stateactions;

import com.pengrad.telegrambot.TelegramBot;
import com.studyexchange.core.UserState;
import com.studyexchange.service.UserService;

import java.util.Map;

public class StateActionFactory {
    private final Map<UserState, BaseStateAction> stateToAction;

    public StateActionFactory(TelegramBot bot, UserService userService) {
        stateToAction = Map.of(
                UserState.NO_NAME, new NoNameAction(bot, userService),
                UserState.NO_PHOTO, new NoPhotoAction(bot, userService)
        );
    }

    public BaseStateAction stateActionFrom(UserState userState) {
        return switch (userState) {
            case NO_NAME -> stateToAction.get(UserState.NO_NAME);
            case NO_PHOTO -> stateToAction.get(UserState.NO_PHOTO);
            case ENTERS_QUERY -> stateToAction.get(UserState.ENTERS_QUERY);
            case VIEWS_PROFILES -> stateToAction.get(UserState.VIEWS_PROFILES);
        };
    }
}
