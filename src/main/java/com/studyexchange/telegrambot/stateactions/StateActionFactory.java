package com.studyexchange.telegrambot.stateactions;

import com.pengrad.telegrambot.TelegramBot;
import com.studyexchange.core.UserState;
import com.studyexchange.service.HelpRequestService;
import com.studyexchange.service.UserService;
import com.studyexchange.telegrambot.stateactions.educational.FillHelpDescriptionEducational;
import com.studyexchange.telegrambot.stateactions.educational.FillHelpPhotosEducational;
import com.studyexchange.telegrambot.stateactions.educational.RequestHelpEducational;

import java.util.Map;

public class StateActionFactory {
    private final Map<UserState, BaseStateAction> stateToAction;

    public StateActionFactory(
        TelegramBot bot,
        UserService userService,
        HelpRequestService helpRequestService
    ) {
        stateToAction = Map.of(
            UserState.NO_NAME_INTRO, new NoNameIntroAction(bot, userService),
            UserState.REQUEST_HELP_EDUCATIONAL, new RequestHelpEducational(
                bot,
                userService,
                helpRequestService
            ),
            UserState.FILL_HELP_DESCRIPTION_EDUCATIONAL, new FillHelpDescriptionEducational(
                bot,
                userService,
                helpRequestService
            ),
            UserState.FILL_HELP_PHOTOS_EDUCATIONAL, new FillHelpPhotosEducational(
                bot,
                userService,
                helpRequestService
            )
        );
    }

    public BaseStateAction stateActionFrom(UserState userState) {
        //don't collapse switch (to force fill new UserState)
        return switch (userState) {
            case NO_NAME_INTRO -> stateToAction.get(UserState.NO_NAME_INTRO);
            case REQUEST_HELP_EDUCATIONAL -> stateToAction.get(UserState.REQUEST_HELP_EDUCATIONAL);
            case FILL_HELP_DESCRIPTION_EDUCATIONAL -> stateToAction.get(UserState.FILL_HELP_DESCRIPTION_EDUCATIONAL);
            case FILL_HELP_PHOTOS_EDUCATIONAL -> stateToAction.get(UserState.FILL_HELP_PHOTOS_EDUCATIONAL);
        };
    }
}
