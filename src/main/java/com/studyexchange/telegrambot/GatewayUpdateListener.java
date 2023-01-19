package com.studyexchange.telegrambot;

import com.google.gson.Gson;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class GatewayUpdateListener implements UpdatesListener {
    private final UpdateReplier updateReplier;
    private final String botToken;

    private final OkHttpClient client;
    private final Gson gson;

    public GatewayUpdateListener(
            UpdateReplier updateReplier,
            String botToken
    ) throws IOException {
        this.updateReplier = updateReplier;
        this.botToken = botToken;
        client = new OkHttpClient();
        gson = new Gson();
        dropUpdatesBeforeServerStart();
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            updateReplier.replyUpdate(update);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private GetUpdatesResponse getLastUpdate() throws IOException {
        return getUpdates(-1);
    }
    private GetUpdatesResponse getUpdates(int offset) throws IOException {
        Request request = new Request.Builder()
                .url("https://api.telegram.org/bot" + botToken + "/getUpdates?offset=" + offset)
                .build();
        Call call = client.newCall(request);
        Response response = call.execute();
        return gson.fromJson(Objects.requireNonNull(response.body()).string(), GetUpdatesResponse.class);
    }

    private void dropUpdatesBeforeIdInclusive(int updateId) throws IOException {
        getUpdates(updateId + 1);
    }

    private void dropUpdatesBeforeServerStart() throws IOException {
        GetUpdatesResponse lastUpdateResponse = getLastUpdate();
        List<Update> updatesResponse = lastUpdateResponse.updates();
        if (updatesResponse == null || updatesResponse.isEmpty()) {
            return;
        }
        assert updatesResponse.size() == 1;
        Update lastUpdate = updatesResponse.get(0);
        dropUpdatesBeforeIdInclusive(lastUpdate.updateId());
    }
}
