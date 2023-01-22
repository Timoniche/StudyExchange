package com.studyexchange.telegrambot.stateactions;

import com.pengrad.telegrambot.TelegramBot;

public abstract class BaseStateAction implements StateAction {
    protected final TelegramBot bot;

    public static String NEXT_LINE = System.lineSeparator();

    public BaseStateAction(
        TelegramBot bot
    ) {
        this.bot = bot;
    }
}
