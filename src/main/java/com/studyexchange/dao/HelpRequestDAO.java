package com.studyexchange.dao;

import com.studyexchange.core.HelpRequest;

public interface HelpRequestDAO {

    HelpRequest findLastHelpRequestByChatId(long chatId);

    void putHelpRequest(HelpRequest helpRequest);

    void updateHelpRequest(HelpRequest helpRequest);
}
