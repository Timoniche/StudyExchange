package com.studyexchange.dao.inmemory;

import com.studyexchange.core.HelpRequest;
import com.studyexchange.dao.HelpRequestDAO;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryHelpRequestDAO implements HelpRequestDAO {
    private final AtomicLong lastHelpRequestId;
    private final Map<Long, HelpRequest> helpRequestById;

    public InMemoryHelpRequestDAO() {
        this.lastHelpRequestId = new AtomicLong(0);
        this.helpRequestById = new HashMap<>();
    }

    @Override
    public HelpRequest findLastHelpRequestByChatId(long chatId) {
        return helpRequestById.values()
            .stream()
            .filter(r -> r.getChatId() == chatId)
            .max(Comparator.comparingLong(HelpRequest::getHelpRequestId))
            .orElse(null);
    }

    @Override
    public void putHelpRequest(HelpRequest helpRequest) {
        long helpRequestId = generateNextId();
        helpRequest.setHelpRequestId(helpRequestId);
        helpRequestById.put(helpRequestId, helpRequest);
    }

    @Override
    public void updateHelpRequest(HelpRequest helpRequest) {
        helpRequestById.put(helpRequest.getHelpRequestId(), helpRequest);
    }

    private long generateNextId() {
        return lastHelpRequestId.addAndGet(1);
    }
}
