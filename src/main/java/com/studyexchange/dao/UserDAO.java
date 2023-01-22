package com.studyexchange.dao;

import com.studyexchange.core.User;

public interface UserDAO {

    User findUserByChatId(long chatId);

    void putUser(User user);
}
