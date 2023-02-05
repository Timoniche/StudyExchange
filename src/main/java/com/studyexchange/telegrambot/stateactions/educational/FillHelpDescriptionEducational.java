package com.studyexchange.telegrambot.stateactions.educational;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.studyexchange.core.HelpRequest;
import com.studyexchange.core.Subject;
import com.studyexchange.core.User;
import com.studyexchange.core.UserState;
import com.studyexchange.service.HelpRequestService;
import com.studyexchange.service.UserService;
import com.studyexchange.telegrambot.stateactions.BaseStateAction;

import java.util.Optional;

import static com.studyexchange.service.HelpRequestService.checkHelpRequestNotNullOrThrow;
import static com.studyexchange.service.UserService.checkUserNotNullOrThrow;

public class FillHelpDescriptionEducational extends BaseStateAction {
    private static String fillDescriptionText(Subject subject) {
        return "Опиши подробно задачу"
            + (subject == Subject.OTHER ? "" : " по " + subject.dativeCase())
            + ", с который тебе нужна помощь";
    }

    private static final String EMPTY_DESCRIPTION_MESSAGE = ""
        + "Получил только пустое сообщение. Можешь ввести описание задачи еще раз?";

    private final HelpRequestService helpRequestService;

    public FillHelpDescriptionEducational(
        TelegramBot bot,
        UserService userService,
        HelpRequestService helpRequestService
    ) {
        super(bot, userService);
        this.helpRequestService = helpRequestService;
    }

    @Override
    public int setupStateAndAskQuestions(long chatId) {
        User user = userService.findUserByChatId(chatId);
        checkUserNotNullOrThrow(user, UserState.FILL_HELP_DESCRIPTION_EDUCATIONAL);
        userService.updateUser(user, u -> u.setUserState(UserState.FILL_HELP_DESCRIPTION_EDUCATIONAL));

        HelpRequest lastHelpRequest = helpRequestService.findLastHelpRequestByChatId(chatId);
        checkHelpRequestNotNullOrThrow(lastHelpRequest, UserState.FILL_HELP_DESCRIPTION_EDUCATIONAL);
        Subject subject = lastHelpRequest.getSubject();
        SendResponse questionMessage = bot.execute(new SendMessage(chatId, fillDescriptionText(subject)));
        return questionMessage.message().date();
    }

    @Override
    public Optional<UserState> processAnswerAndReturnNextStateToSetup(Update update) {
        if (update.message() == null) {
            return Optional.empty();
        }
        long chatId = update.message().chat().id();
        String description = update.message().text();
        if (description == null || description.isBlank()) {
            bot.execute(new SendMessage(chatId, EMPTY_DESCRIPTION_MESSAGE));
            return Optional.empty();
        }
        HelpRequest lastHelpRequest = helpRequestService.findLastHelpRequestByChatId(chatId);
        checkHelpRequestNotNullOrThrow(lastHelpRequest, UserState.FILL_HELP_DESCRIPTION_EDUCATIONAL);

        helpRequestService.updateHelpRequest(lastHelpRequest, r -> r.setDescription(description));
        return Optional.of(UserState.FILL_HELP_PHOTOS_EDUCATIONAL);
    }
}
