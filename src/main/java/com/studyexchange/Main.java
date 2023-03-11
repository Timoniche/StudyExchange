package com.studyexchange;

import com.pengrad.telegrambot.TelegramBot;
import com.studyexchange.core.Matcher;
import com.studyexchange.dao.inmemory.InMemoryHelpRequestDAO;
import com.studyexchange.dao.inmemory.InMemoryUserDAO;
import com.studyexchange.service.HelpRequestService;
import com.studyexchange.service.UserService;
import com.studyexchange.telegrambot.GatewayUpdateListener;
import com.studyexchange.telegrambot.UpdateReplier;
import com.studyexchange.telegrambot.stateactions.StateActionFactory;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Specify bot token");
        }
        String token = args[0];
        TelegramBot bot = new TelegramBot(token);
        UserService userService = new UserService(new InMemoryUserDAO());
        HelpRequestService helpRequestService = new HelpRequestService(new InMemoryHelpRequestDAO());
        StateActionFactory stateActionFactory = new StateActionFactory(
            bot,
            userService,
            helpRequestService,
            new Matcher()
        );
        UpdateReplier updateReplier = new UpdateReplier(
            userService,
            stateActionFactory,
            bot
        );
        GatewayUpdateListener gatewayUpdateListener = new GatewayUpdateListener(updateReplier, bot);
        bot.setUpdatesListener(gatewayUpdateListener);
    }
}
