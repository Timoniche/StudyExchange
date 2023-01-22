package com.studyexchange.telegrambot.stateactions.educational;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.studyexchange.core.HelpRequest;
import com.studyexchange.core.Subject;
import com.studyexchange.core.User;
import com.studyexchange.core.UserState;
import com.studyexchange.service.HelpRequestService;
import com.studyexchange.service.UserService;
import com.studyexchange.telegrambot.stateactions.BaseStateAction;

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
    private static String firstRequestingHelpText(String userName) {
        return ""
            + "Рад знакомству, " + userName + "!" + NEXT_LINE
            + NEXT_LINE
            + "Давай составим нашу первую просьбу о помощи. " + NEXT_LINE
            + NEXT_LINE
            + "Выбери, какой предмет вызывает у тебя трудности. "
            + "Можно нажать кнопку \"" + OTHER.getName() + "\", "
            + "если нужна помощь с организацией чего-либо, "
            + "или предложенные варианты не подходят";
    }

    private static final String WRONG_SUBJECT_NAME = ""
        + "К сожалению, такого предмета еще нет. Пожалуйста, выбери предмет из списка";

    private static final ReplyKeyboardMarkup SUBJECTS_KEYBOARD =
        new ReplyKeyboardMarkup(
            new String[]{MATHEMATICS.getName(), PHYSICS.getName(), INFORMATICS.getName()},
            new String[]{RUSSIAN.getName(), ENGLISH.getName()},
            new String[]{CHEMISTRY.getName(), LITERATURE.getName(), GEOGRAPHY.getName()},
            new String[]{SOCIAL.getName(), BIOLOGY.getName(), HISTORY.getName()},
            new String[]{OTHER.getName()}
        )
            .oneTimeKeyboard(true)
            .resizeKeyboard(true);

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
    public void setupStateAndAskQuestions(long chatId) {
        User user = userService.findUserByChatId(chatId);
        checkUserNotNullOrThrow(user, UserState.REQUEST_HELP_EDUCATIONAL);
        userService.updateUser(user, u -> u.setUserState(UserState.REQUEST_HELP_EDUCATIONAL));

        String userName = user.getName();
        if (userName == null) {
            throw new IllegalStateException("UserName can't be null in REQUEST_HELP_EDU state, chatId: " + chatId);
        }

        bot.execute(
            new SendMessage(chatId, firstRequestingHelpText(userName))
                .replyMarkup(SUBJECTS_KEYBOARD)
        );
    }

    @Override
    public UserState processAnswerAndReturnNextStateToSetup(Update update) {
        long chatId = update.message().chat().id();
        String subjectAnswer = update.message().text();
        Subject subject = Subject.fromName(subjectAnswer);
        if (subject == null) {
            bot.execute(
                new SendMessage(chatId, WRONG_SUBJECT_NAME)
                    .replyMarkup(SUBJECTS_KEYBOARD)
            );
            return null;
        }
        HelpRequest helpRequest = new HelpRequest(chatId, subject);
        helpRequestService.putHelpRequest(helpRequest);
        return UserState.FILL_HELP_DESCRIPTION_EDUCATIONAL;
    }
}
