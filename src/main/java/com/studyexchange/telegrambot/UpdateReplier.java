package com.studyexchange.telegrambot;

import com.pengrad.telegrambot.model.Update;
import com.studyexchange.core.User;
import com.studyexchange.core.UserState;
import com.studyexchange.service.UserService;
import com.studyexchange.telegrambot.stateactions.StateActionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

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
        long chatId = update.message().chat().id();
        User user = userService.findUserByChatId(chatId);
        if (user == null) {
            int botReplyDate = stateActionFactory.stateActionFrom(UserState.NO_NAME_INTRO)
                .setupStateAndAskQuestions(chatId);
            userService.updateUser(chatId, u -> u.setLastBotAnswerDate(botReplyDate));
            return;
        }

        if (user.getLastBotAnswerDate() >= update.message().date()) {
            log.warn("UpdateId {} is ignored", update.updateId());
            return;
        }

        Optional<UserState> nextUserStateToSetup = stateActionFactory.stateActionFrom(user.getUserState())
            .processAnswerAndReturnNextStateToSetup(update);
        if (nextUserStateToSetup.isPresent()) {
            int botReplyDate = stateActionFactory.stateActionFrom(nextUserStateToSetup.get())
                .setupStateAndAskQuestions(chatId);
            userService.updateUser(chatId, u -> u.setLastBotAnswerDate(botReplyDate));
        }
    }
}
