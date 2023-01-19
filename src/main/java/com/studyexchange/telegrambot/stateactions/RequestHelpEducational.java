package com.studyexchange.telegrambot.stateactions;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.studyexchange.core.User;
import com.studyexchange.core.UserState;
import com.studyexchange.service.UserService;

import static com.studyexchange.service.UserService.checkUserNotNullOrThrow;

public class RequestHelpEducational extends BaseStateAction {
    private static String firstRequestingHelpText(String userName) {
        return ""
                + "Рад знакомству, " + userName + "!" + NEXT_LINE
                + NEXT_LINE
                + "Давай составим нашу первую просьбу о помощи. " + NEXT_LINE
                + NEXT_LINE
                + "Выбери, какой предмет вызывает у тебя трудности. "
                + "Можно нажать кнопку \"Другое\", если нужна помощь с организацией чего-либо, "
                + "или предложенные варианты не подходят.";
    }

    private static final ReplyKeyboardMarkup SUBJECTS_KEYBOARD =
            new ReplyKeyboardMarkup(
                    new String[]{"Математика", "Химия", "Русский язык"},
                    new String[]{"Физика", "Биология", "Английский язык"},
                    new String[]{"История", "Обществознание", "География"},
                    new String[]{"Информатика", "Литература", "Другое"}
            )
                    .oneTimeKeyboard(true)
                    .resizeKeyboard(true);

    public RequestHelpEducational(TelegramBot bot, UserService userService) {
        super(bot, userService);
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
        return UserState.REQUEST_HELP_EDUCATIONAL;
    }
}
