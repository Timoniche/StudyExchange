package com.studyexchange.telegrambot.stateactions;

import com.pengrad.telegrambot.TelegramBot;
import com.studyexchange.service.UserService;

public abstract class BaseStateAction implements StateAction {
    protected final TelegramBot bot;
    protected final UserService userService;

    public static String NEXT_LINE = System.lineSeparator();

    public BaseStateAction(
        TelegramBot bot,
        UserService userService
    ) {
        this.bot = bot;
        this.userService = userService;
    }
}
