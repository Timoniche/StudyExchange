package com.studyexchange.telegrambot.stateactions.educational;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.studyexchange.core.User;
import com.studyexchange.core.UserState;
import com.studyexchange.service.UserService;
import com.studyexchange.telegrambot.stateactions.BaseStateAction;

import java.util.Optional;

import static com.studyexchange.service.UserService.checkUserNotNullOrThrow;
import static com.studyexchange.telegramapiutils.MessagingUtils.NEXT_LINE;

public class ReadyToSearchEducational extends BaseStateAction {
    private static final String CONGRATULATIONS_TEXT = ""
        + "Мы уже готовы к поиску человека, с кем можно обменяться помощью:)"
        + NEXT_LINE
        + NEXT_LINE
        + "Нажми кнопку \"" + Action.JOIN_THE_QUEUE.getActionText() + "\","
        + " если ты пока не хочешь менять информацию о себе/добавлять новые просьбы о помощи";

    private static final String WRONG_ACTION_TEXT = ""
        + "Пожалуйста, выбери действие из списка";

    private static final ReplyKeyboardMarkup ACTION_BUTTONS =
        new ReplyKeyboardMarkup(
            actionNames(Action.JOIN_THE_QUEUE),
            actionNames(Action.ADD_HELP_REQUEST),
            actionNames(Action.CHANGE_HELP_REQUESTS)
        )
            .oneTimeKeyboard(true)
            .resizeKeyboard(true);

    private static String[] actionNames(Action... actions) {
        String[] names = new String[actions.length];
        for (int i = 0; i < actions.length; i++) {
            names[i] = actions[i].getActionText();
        }
        return names;
    }

    public ReadyToSearchEducational(TelegramBot bot, UserService userService) {
        super(bot, userService);
    }

    @Override
    public int setupStateAndAskQuestions(long chatId) {
        User user = userService.findUserByChatId(chatId);
        checkUserNotNullOrThrow(user, UserState.READY_TO_SEARCH_EDUCATIONAL);
        userService.updateUser(user, u -> u.setUserState(UserState.READY_TO_SEARCH_EDUCATIONAL));

        SendResponse questionMessage = bot.execute(
            new SendMessage(chatId, CONGRATULATIONS_TEXT)
                .replyMarkup(ACTION_BUTTONS)
        );
        return questionMessage.message().date();
    }

    @Override
    public Optional<UserState> processAnswerAndReturnNextStateToSetup(Update update) {
        if (update.message() == null) {
            return Optional.empty();
        }
        long chatId = update.message().chat().id();
        String actionAnswer = update.message().text();
        Action action = Action.fromText(actionAnswer);
        if (action == null) {
            bot.execute(
                new SendMessage(chatId, WRONG_ACTION_TEXT)
                    .replyMarkup(ACTION_BUTTONS)
            );
            return Optional.empty();
        }
        return switch (action) {
            case JOIN_THE_QUEUE -> Optional.of(UserState.APPLY_FILTERS_START_SEARCHING);
            case ADD_HELP_REQUEST, CHANGE_HELP_REQUESTS -> Optional.of(UserState.READY_TO_SEARCH_EDUCATIONAL);
        };
    }

    private enum Action {
        JOIN_THE_QUEUE("Начать поиск!"),
        ADD_HELP_REQUEST("Добавить просьбу о помощи"),
        CHANGE_HELP_REQUESTS("Изменить существующие просьбы о помощи");

        private final String actionText;

        Action(String actionText) {
            this.actionText = actionText;
        }

        public static Action fromText(String text) {
            for (Action action : Action.values()) {
                if (action.getActionText().equalsIgnoreCase(text)) {
                    return action;
                }
            }
            return null;
        }

        public String getActionText() {
            return actionText;
        }
    }
}
