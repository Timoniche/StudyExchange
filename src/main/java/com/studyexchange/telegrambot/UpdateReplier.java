package com.studyexchange.telegrambot;

import com.pengrad.telegrambot.model.Update;
import com.studyexchange.core.User;
import com.studyexchange.core.UserState;
import com.studyexchange.service.UserService;
import com.studyexchange.telegrambot.stateactions.StateActionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UpdateReplier {
    private static final Logger log = LogManager.getLogger(UpdateReplier.class);

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
        if (update.message() == null) {
            return;
        }
        log.info(update.message());
        long chatId = update.message().chat().id();
        User user = userService.findUserByChatId(chatId);
        if (user == null) {
            stateActionFactory.stateActionFrom(UserState.NO_NAME_INTRO).setupStateAndAskQuestions(chatId);
        } else {
            UserState nextUserStateToSetup = stateActionFactory.stateActionFrom(user.getUserState())
                .processAnswerAndReturnNextStateToSetup(update);
            if (nextUserStateToSetup != null) {
                stateActionFactory.stateActionFrom(nextUserStateToSetup).setupStateAndAskQuestions(chatId);
            }
        }
    }
}
