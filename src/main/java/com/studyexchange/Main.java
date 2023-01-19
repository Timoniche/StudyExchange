package com.studyexchange;

import com.pengrad.telegrambot.TelegramBot;
import com.studyexchange.dao.InMemoryUserDAO;
import com.studyexchange.service.UserService;
import com.studyexchange.telegrambot.GatewayUpdateListener;
import com.studyexchange.telegrambot.UpdateReplier;
import com.studyexchange.telegrambot.stateactions.StateActionFactory;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            throw new IllegalArgumentException("Specify bot token");
        }
        String token = args[0];
        TelegramBot bot = new TelegramBot(token);
        UserService userService = new UserService(new InMemoryUserDAO());
        StateActionFactory stateActionFactory = new StateActionFactory(
                bot,
                userService
        );
        UpdateReplier updateReplier = new UpdateReplier(
                userService,
                stateActionFactory
        );
        GatewayUpdateListener gatewayUpdateListener = new GatewayUpdateListener(updateReplier, token);
        bot.setUpdatesListener(gatewayUpdateListener);
    }
}