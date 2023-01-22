package com.studyexchange.service;

import com.studyexchange.core.HelpRequest;
import com.studyexchange.dao.HelpRequestDAO;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class HelpRequestService {
    private final HelpRequestDAO helpRequestDAO;

    public HelpRequestService(HelpRequestDAO helpRequestDAO) {
        this.helpRequestDAO = helpRequestDAO;
    }

    public void putHelpRequest(HelpRequest helpRequest) {
        helpRequestDAO.putHelpRequest(helpRequest);
    }

    public void updateHelpRequest(
        @NotNull HelpRequest helpRequest,
        Consumer<HelpRequest> modification
    ) {
        modification.accept(helpRequest);
        putHelpRequest(helpRequest);
    }

    public HelpRequest findLastHelpRequestByChatId(long chatId) {
        return helpRequestDAO.findLastHelpRequestByChatId(chatId);
    }
}
