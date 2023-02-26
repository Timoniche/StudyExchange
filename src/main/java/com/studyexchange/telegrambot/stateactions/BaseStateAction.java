package com.studyexchange.telegrambot.stateactions;

import com.pengrad.telegrambot.TelegramBot;
import com.studyexchange.service.UserService;

public abstract class BaseStateAction implements StateAction {
    protected final TelegramBot bot;
    protected final UserService userService;

    public static String NEXT_LINE = System.lineSeparator();

    public static String italics(String s) {
        return "_" + escapeMarkdownV2(s) + "_";
    }

    public static String bold(String s) {
        return "*" + escapeMarkdownV2(s) + "*";
    }

    public static String escapeMarkdownV2(String s) {
        String[] escapedSymbols =
            {"_", "*", "[", "]", "(", ")", "~", "`", ">", "#", "+", "-", "=", "|", "{", "}", ".", "!"};
        for (String escapedSymbol : escapedSymbols) {
            if (s.contains(escapedSymbol)) {
                s = s.replace(escapedSymbol, "\\" + escapedSymbol);
            }
        }
        return s;
    }

    public BaseStateAction(
        TelegramBot bot,
        UserService userService
    ) {
        this.bot = bot;
        this.userService = userService;
    }
}
