package com.studyexchange.dao;

import com.studyexchange.core.User;

public interface UserDAO {

    User findUserByChatId(long chatId);

    void putUserByChatId(User user);
}
