package com.studyexchange.telegrambot.stateactions;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.studyexchange.core.User;
import com.studyexchange.core.UserState;
import com.studyexchange.service.UserService;

public class NoNameIntroAction extends BaseStateAction {
    private static final String GREETINGS_TEXT = "" +
            "Привет! Давай знакомиться)" + NEXT_LINE +
            NEXT_LINE +
            "Я бот, помогающий людям обмениваться знаниями по разным предметам." + NEXT_LINE +
            "Здесь ты сможешь выложить просьбу о помощи и обменять ее на помощь другому человеку." + NEXT_LINE +
            NEXT_LINE +
            "Как тебя зовут?";

    private static final String EMPTY_NAME_TEXT = "" +
            "Получил только пустое сообщение. Можешь ввести свое имя еще раз?";

    public NoNameIntroAction(TelegramBot bot, UserService userService) {
        super(bot, userService);
    }

    @Override
    public void setupStateAndAskQuestions(long chatId) {
        User user = userService.findUserByChatId(chatId);
        if (user == null) {
            userService.putUserByChatId(User.newUser(chatId, UserState.NO_NAME_INTRO));
        } else {
            userService.updateUser(user, u -> u.setUserState(UserState.NO_NAME_INTRO));
        }

        bot.execute(new SendMessage(chatId, GREETINGS_TEXT));
    }

    @Override
    public UserState processAnswerAndReturnNextStateToSetup(Update update) {
        long chatId = update.message().chat().id();
        String newName = update.message().text();
        if (newName == null || newName.isBlank()) {
            bot.execute(new SendMessage(chatId, EMPTY_NAME_TEXT));
            return null;
        }
        User user = userService.findUserByChatId(chatId);
        userService.updateUser(user, u -> u.setName(newName));

        return UserState.REQUEST_HELP_EDUCATIONAL;
    }
}
