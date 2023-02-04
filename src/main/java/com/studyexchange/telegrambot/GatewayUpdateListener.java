package com.studyexchange.telegrambot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.response.GetUpdatesResponse;

import java.util.List;

public class GatewayUpdateListener implements UpdatesListener {
    private final UpdateReplier updateReplier;

    public GatewayUpdateListener(
        UpdateReplier updateReplier,
        TelegramBot bot
    ) {
        this.updateReplier = updateReplier;
        dropUpdatesBeforeServerStart(bot);
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            updateReplier.replyUpdate(update);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private GetUpdatesResponse getLastUpdate(TelegramBot bot) {
        return bot.execute(new GetUpdates().offset(-1));
    }

    private void dropUpdatesBeforeIdInclusive(TelegramBot bot, int updateId) {
        bot.execute(new GetUpdates().offset(updateId + 1));
    }

    private void dropUpdatesBeforeServerStart(TelegramBot bot) {
        GetUpdatesResponse lastUpdateResponse = getLastUpdate(bot);
        List<Update> updatesResponse = lastUpdateResponse.updates();
        if (updatesResponse == null || updatesResponse.isEmpty()) {
            return;
        }
        assert updatesResponse.size() == 1;
        Update lastUpdate = updatesResponse.get(0);
        dropUpdatesBeforeIdInclusive(bot, lastUpdate.updateId());
    }
}
