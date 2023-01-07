package com.studyexchange.telegrambot.stateactions;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.studyexchange.core.User;
import com.studyexchange.core.UserState;
import com.studyexchange.service.UserService;

public class NoNameAction extends BaseStateAction {
    public NoNameAction(
            TelegramBot bot,
            UserService userService
    ) {
        super(bot, userService);
    }

    @Override
    public void setupStateAndAskQuestions(long chatId) {
        User user = userService.findUserByChatId(chatId);
        if (user == null) {
            userService.putUserByChatId(User.newUser(chatId, UserState.NO_NAME));
        } else {
            userService.updateUser(user, u -> u.setUserState(UserState.NO_NAME));
        }

        bot.execute(new SendMessage(chatId, "Как вас зовут?"));
    }

    @Override
    public UserState processAnswerAndReturnNextStateToSetup(Update update) {
        long chatId = update.message().chat().id();
        String newName = update.message().text();
        if (newName == null) {
            return null;
        }
        User user = userService.findUserByChatId(chatId);
        userService.updateUser(user, u -> u.setName(newName));

        return UserState.NO_PHOTO;
    }
}
