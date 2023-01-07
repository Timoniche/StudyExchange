package com.studyexchange.telegrambot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;

import java.util.List;

public class GatewayUpdateListener implements UpdatesListener {
    private final UpdateReplier updateReplier;

    public GatewayUpdateListener(
            UpdateReplier updateReplier
    ) {
        this.updateReplier = updateReplier;
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            updateReplier.replyUpdate(update);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
