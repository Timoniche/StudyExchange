package com.studyexchange;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.studyexchange.service.UserService;

import java.util.List;

public class GatewayUpdateListener implements UpdatesListener {
    private final TelegramBot bot;
    private final UserService userService;

    public GatewayUpdateListener(
            TelegramBot telegramBot,
            UserService userService
    ) {
        this.bot = telegramBot;
        this.userService = userService;
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            handleUpdate(update);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public void handleUpdate(Update update) {
        long chatId = update.message().chat().id();
        Message message = update.message();

        bot.execute(new SendMessage(chatId, message.text() == null ? "no msg" : message.text()));
    }
}
