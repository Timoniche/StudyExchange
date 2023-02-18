package com.studyexchange.telegrambot.stateactions.educational;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.studyexchange.core.Subject;
import com.studyexchange.core.User;
import com.studyexchange.core.UserState;
import com.studyexchange.service.UserService;
import com.studyexchange.telegrambot.stateactions.BaseStateAction;

import java.util.EnumSet;
import java.util.Optional;

import static com.studyexchange.service.UserService.checkUserNotNullOrThrow;

public class FillTopicsCanHelpEducational extends BaseStateAction {
    private static final String TICK = "✅";
    private static final String CROSS = "✖️";
    private static final String COMPLETE_SELECTION = "complete";

    private static final String ASK_SUBJECTS_CAN_HELP_WITH = ""
        + "Теперь давай укажем, с какими предметами ты хотел бы помогать другим людям."
        + NEXT_LINE
        + "Нажми на ⛔, когда завершишь свой выбор (нужно выбрать хотя бы один предмет)";

    private static InlineKeyboardMarkup subjectsInlineKeyboard(EnumSet<Subject> subjectsCanHelpWith) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        if (!subjectsCanHelpWith.isEmpty()) {
            keyboard.addRow(new InlineKeyboardButton("⛔️ Завершить выбор").callbackData(COMPLETE_SELECTION));
        }
        for (Subject subject : Subject.values()) {
            String subjectName = subject.getName();
            String buttonText;
            if (subjectsCanHelpWith.contains(subject)) {
                buttonText = TICK + " " + subjectName;
            } else {
                buttonText = CROSS + " " + subjectName;
            }
            keyboard.addRow(new InlineKeyboardButton(buttonText).callbackData(subjectName));
        }
        return keyboard;
    }

    public FillTopicsCanHelpEducational(
        TelegramBot bot,
        UserService userService
    ) {
        super(bot, userService);
    }

    @Override
    public int setupStateAndAskQuestions(long chatId) {
        User user = userService.findUserByChatId(chatId);
        checkUserNotNullOrThrow(user, UserState.FILL_TOPICS_CAN_HELP_EDUCATIONAL);
        userService.updateUser(user, u -> u.setUserState(UserState.FILL_TOPICS_CAN_HELP_EDUCATIONAL));

        SendResponse questionMessage = bot.execute(
            new SendMessage(chatId, ASK_SUBJECTS_CAN_HELP_WITH)
                .replyMarkup(subjectsInlineKeyboard(user.getSubjectsCanHelpWith()))
        );
        return questionMessage.message().date();
    }

    @Override
    public Optional<UserState> processAnswerAndReturnNextStateToSetup(Update update) {
        CallbackQuery callback = update.callbackQuery();
        if (callback == null) {
            return Optional.empty();
        }
        long chatId = callback.message().chat().id();
        if (callback.data().equals(COMPLETE_SELECTION)) {
            bot.execute(new SendMessage(chatId, "COMPLETED"));
            return Optional.empty();
        }
        Subject selectedSubject = Subject.fromName(callback.data());

        if (selectedSubject == null) {
            return Optional.empty();
        }

        User user = userService.findUserByChatId(chatId);
        checkUserNotNullOrThrow(user, UserState.FILL_TOPICS_CAN_HELP_EDUCATIONAL);

        user.changeSubjectPresence(selectedSubject);

        bot.execute(
            new EditMessageReplyMarkup(chatId, callback.message().messageId())
                .replyMarkup(subjectsInlineKeyboard(user.getSubjectsCanHelpWith()))
        );

        return Optional.empty();
    }
}
