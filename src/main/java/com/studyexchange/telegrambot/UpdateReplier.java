package com.studyexchange.telegrambot;

import com.pengrad.telegrambot.model.Update;
import com.studyexchange.core.User;
import com.studyexchange.core.UserState;
import com.studyexchange.service.UserService;
import com.studyexchange.telegrambot.stateactions.StateActionFactory;

public class UpdateReplier {
    private final UserService userService;
    private final StateActionFactory stateActionFactory;

    public UpdateReplier(
            UserService userService,
            StateActionFactory stateActionFactory
    ) {
        this.userService = userService;
        this.stateActionFactory = stateActionFactory;
    }

    public void replyUpdate(Update update) {
        long chatId = update.message().chat().id();
        User user = userService.findUserByChatId(chatId);
        if (user == null) {
            stateActionFactory.stateActionFrom(UserState.NO_NAME).setupStateAndAskQuestions(chatId);
        } else {
            UserState nextUserStateToSetup = stateActionFactory.stateActionFrom(user.getUserState())
                    .processAnswerAndReturnNextStateToSetup(update);
            if (nextUserStateToSetup == null) {
                //todo:
                throw new AssertionError();
            }
            stateActionFactory.stateActionFrom(nextUserStateToSetup).setupStateAndAskQuestions(chatId);
        }
    }
}