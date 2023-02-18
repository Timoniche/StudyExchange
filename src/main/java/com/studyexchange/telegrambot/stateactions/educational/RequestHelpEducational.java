package com.studyexchange.telegrambot.stateactions.educational;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
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

import static com.studyexchange.core.Subject.BIOLOGY;
import static com.studyexchange.core.Subject.CHEMISTRY;
import static com.studyexchange.core.Subject.ENGLISH;
import static com.studyexchange.core.Subject.GEOGRAPHY;
import static com.studyexchange.core.Subject.HISTORY;
import static com.studyexchange.core.Subject.INFORMATICS;
import static com.studyexchange.core.Subject.LITERATURE;
import static com.studyexchange.core.Subject.MATHEMATICS;
import static com.studyexchange.core.Subject.OTHER;
import static com.studyexchange.core.Subject.PHYSICS;
import static com.studyexchange.core.Subject.RUSSIAN;
import static com.studyexchange.core.Subject.SOCIAL;
import static com.studyexchange.service.UserService.checkUserNotNullOrThrow;

public class RequestHelpEducational extends BaseStateAction {
    private static final String REQUESTING_HELP_TEXT = ""
            + "Давай составим нашу первую просьбу о помощи. " + NEXT_LINE
            + NEXT_LINE
            + "Выбери, какой предмет вызывает у тебя трудности. "
            + "Можно нажать кнопку \"" + OTHER.getName() + "\", "
            + "если нужна помощь с организацией чего-либо, "
            + "или предложенные варианты не подходят";

    private static final String WRONG_SUBJECT_NAME = ""
        + "К сожалению, такого предмета еще нет. Пожалуйста, выбери предмет из списка";

    private static final ReplyKeyboardMarkup SUBJECTS_KEYBOARD =
        new ReplyKeyboardMarkup(
            subjectNames(MATHEMATICS, PHYSICS, INFORMATICS),
            subjectNames(CHEMISTRY, LITERATURE, GEOGRAPHY),
            subjectNames(SOCIAL, BIOLOGY, HISTORY),
            subjectNames(RUSSIAN, ENGLISH, OTHER)
        )
            .oneTimeKeyboard(true)
            .resizeKeyboard(true);

    private static String[] subjectNames(Subject... subjects) {
        String[] names = new String[subjects.length];
        for (int i = 0; i < subjects.length; i++) {
            names[i] = subjects[i].getName();
        }
        return names;
    }

    private final HelpRequestService helpRequestService;

    public RequestHelpEducational(
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
        checkUserNotNullOrThrow(user, UserState.REQUEST_HELP_EDUCATIONAL);
        userService.updateUser(user, u -> u.setUserState(UserState.REQUEST_HELP_EDUCATIONAL));

        String userName = user.getName();
        if (userName == null) {
            throw new IllegalStateException("UserName can't be null in REQUEST_HELP_EDU state, chatId: " + chatId);
        }

        SendResponse questionMessage = bot.execute(
            new SendMessage(chatId, REQUESTING_HELP_TEXT)
                .replyMarkup(SUBJECTS_KEYBOARD)
        );
        return questionMessage.message().date();
    }

    @Override
    public Optional<UserState> processAnswerAndReturnNextStateToSetup(Update update) {
        if (update.message() == null) {
            return Optional.empty();
        }
        long chatId = update.message().chat().id();
        String subjectAnswer = update.message().text();
        Subject subject = Subject.fromName(subjectAnswer);
        if (subject == null) {
            bot.execute(
                new SendMessage(chatId, WRONG_SUBJECT_NAME)
                    .replyMarkup(SUBJECTS_KEYBOARD)
            );
            return Optional.empty();
        }
        HelpRequest helpRequest = new HelpRequest(chatId, subject);
        helpRequestService.putHelpRequest(helpRequest);
        return Optional.of(UserState.FILL_HELP_DESCRIPTION_EDUCATIONAL);
    }
}
