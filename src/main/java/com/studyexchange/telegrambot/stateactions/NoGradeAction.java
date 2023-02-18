package com.studyexchange.telegrambot.stateactions;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.studyexchange.core.Grade;
import com.studyexchange.core.User;
import com.studyexchange.core.UserState;
import com.studyexchange.service.UserService;

import java.util.Optional;

import static com.studyexchange.core.Grade.EIGHTH;
import static com.studyexchange.core.Grade.ELEVENTH;
import static com.studyexchange.core.Grade.FIFTH;
import static com.studyexchange.core.Grade.FIRST;
import static com.studyexchange.core.Grade.FOURTH;
import static com.studyexchange.core.Grade.GRADUATED;
import static com.studyexchange.core.Grade.NINTH;
import static com.studyexchange.core.Grade.SECOND;
import static com.studyexchange.core.Grade.SEVENTH;
import static com.studyexchange.core.Grade.SIXTH;
import static com.studyexchange.core.Grade.TENTH;
import static com.studyexchange.core.Grade.THIRD;
import static com.studyexchange.service.UserService.checkUserNotNullOrThrow;

public class NoGradeAction extends BaseStateAction {
    private static String askGradeText(String userName) {
        return ""
            + "Рад знакомству, " + userName + "!" + NEXT_LINE
            + NEXT_LINE
            + "Пожалуйста, отметь, в каком ты сейчас классе";
    }

    private static final String WRONG_GRADE_NAME = ""
        + "Выбери класс из списка или отметь \"Закончил(а) школу\"";

    public NoGradeAction(TelegramBot bot, UserService userService) {
        super(bot, userService);
    }

    private static final ReplyKeyboardMarkup GRADES_KEYBOARD =
        new ReplyKeyboardMarkup(
            gradeNames(FIRST, SECOND, THIRD),
            gradeNames(FOURTH, FIFTH, SIXTH),
            gradeNames(SEVENTH, EIGHTH, NINTH),
            gradeNames(TENTH, ELEVENTH, GRADUATED)
        )
            .oneTimeKeyboard(true)
            .resizeKeyboard(true);

    private static String[] gradeNames(Grade... grades) {
        String[] names = new String[grades.length];
        for (int i = 0; i < grades.length; i++) {
            names[i] = grades[i].getGradeName();
        }
        return names;
    }

    @Override
    public int setupStateAndAskQuestions(long chatId) {
        User user = userService.findUserByChatId(chatId);
        checkUserNotNullOrThrow(user, UserState.NO_GRADE);
        userService.updateUser(user, u -> u.setUserState(UserState.NO_GRADE));

        SendResponse questionMessage = bot.execute(
            new SendMessage(chatId, askGradeText(user.getName()))
                .replyMarkup(GRADES_KEYBOARD)
        );
        return questionMessage.message().date();
    }

    @Override
    public Optional<UserState> processAnswerAndReturnNextStateToSetup(Update update) {
        if (update.message() == null) {
            return Optional.empty();
        }
        long chatId = update.message().chat().id();
        String gradeAnswer = update.message().text();
        Grade grade = Grade.fromName(gradeAnswer);
        if (grade == null) {
            bot.execute(
                new SendMessage(chatId, WRONG_GRADE_NAME)
                    .replyMarkup(GRADES_KEYBOARD)
            );
            return Optional.empty();
        }
        userService.updateUser(chatId, u -> u.setGrade(grade));
        return Optional.of(UserState.REQUEST_HELP_EDUCATIONAL);
    }
}
