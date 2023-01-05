package com.studyexchange;

import com.pengrad.telegrambot.TelegramBot;
import com.studyexchange.dao.InMemoryUserDAO;
import com.studyexchange.service.UserService;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Specify bot token");
        }
        String token = args[0];
        TelegramBot bot = new TelegramBot(token);
        UserService userService = new UserService(new InMemoryUserDAO());
        GatewayUpdateListener gatewayUpdateListener = new GatewayUpdateListener(
                bot,
                userService
        );
        bot.setUpdatesListener(gatewayUpdateListener);
    }
}