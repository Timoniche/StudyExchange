package com.studyexchange.telegrambot.stateactions;

import com.pengrad.telegrambot.TelegramBot;
import com.studyexchange.core.Matcher;
import com.studyexchange.core.UserState;
import com.studyexchange.service.HelpRequestService;
import com.studyexchange.service.UserService;
import com.studyexchange.telegrambot.stateactions.educational.FillHelpDescriptionEducational;
import com.studyexchange.telegrambot.stateactions.educational.FillHelpPhotosEducational;
import com.studyexchange.telegrambot.stateactions.educational.FillTopicsCanHelpEducational;
import com.studyexchange.telegrambot.stateactions.educational.ReadyToSearchEducational;
import com.studyexchange.telegrambot.stateactions.educational.RequestHelpEducational;

import java.util.HashMap;
import java.util.Map;

public class StateActionFactory {
    private final Map<UserState, BaseStateAction> stateToAction;

    public StateActionFactory(
        TelegramBot bot,
        UserService userService,
        HelpRequestService helpRequestService,
        Matcher matcher
    ) {
        stateToAction = new HashMap<>();
        for (UserState userState : UserState.values()) {
            BaseStateAction stateAction = generateStateActionFrom(
                userState,
                bot,
                userService,
                helpRequestService,
                matcher
            );
            stateToAction.put(userState, stateAction);
        }
    }

    private static BaseStateAction generateStateActionFrom(
        UserState userState,
        TelegramBot bot,
        UserService userService,
        HelpRequestService helpRequestService,
        Matcher matcher
    ) {
        return switch (userState) {
            case NO_NAME_INTRO -> new NoNameIntroAction(bot, userService);
            case NO_GRADE -> new NoGradeAction(bot, userService);
            case REQUEST_HELP_EDUCATIONAL -> new RequestHelpEducational(
                bot,
                userService,
                helpRequestService
            );
            case FILL_HELP_DESCRIPTION_EDUCATIONAL -> new FillHelpDescriptionEducational(
                bot,
                userService,
                helpRequestService
            );
            case FILL_HELP_PHOTOS_EDUCATIONAL -> new FillHelpPhotosEducational(
                bot,
                userService,
                helpRequestService
            );
            case FILL_TOPICS_CAN_HELP_EDUCATIONAL -> new FillTopicsCanHelpEducational(bot, userService);
            case READY_TO_SEARCH_EDUCATIONAL -> new ReadyToSearchEducational(bot, userService);
            case APPLY_FILTERS_START_SEARCHING -> new ApplyFiltersStartSearchingAction(
                bot,
                userService,
                matcher
            );
        };
    }

    public BaseStateAction stateActionFrom(UserState userState) {
        return stateToAction.get(userState);
    }
}
