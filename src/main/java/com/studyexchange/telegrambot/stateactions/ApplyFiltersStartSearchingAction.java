package com.studyexchange.telegrambot.stateactions;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.studyexchange.core.Matcher;
import com.studyexchange.core.User;
import com.studyexchange.core.UserState;
import com.studyexchange.service.UserService;

import java.util.Optional;

import static com.studyexchange.service.UserService.checkUserNotNullOrThrow;

public class ApplyFiltersStartSearchingAction extends BaseStateAction {
    private final Matcher matcher;

    public ApplyFiltersStartSearchingAction(
        TelegramBot bot,
        UserService userService,
        Matcher matcher
    ) {
        super(bot, userService);
        this.matcher = matcher;
    }

    @Override
    public int setupStateAndAskQuestions(long chatId) {
        User user = userService.findUserByChatId(chatId);
        checkUserNotNullOrThrow(user, UserState.APPLY_FILTERS_START_SEARCHING);
        userService.updateUser(user, u -> u.setUserState(UserState.APPLY_FILTERS_START_SEARCHING));

        return 0;
    }

    @Override
    public Optional<UserState> processAnswerAndReturnNextStateToSetup(Update update) {
        return Optional.empty();
    }
}
