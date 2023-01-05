package com.studyexchange.service;

import com.studyexchange.core.User;
import com.studyexchange.dao.UserDAO;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    User findUserByChatId(long chatId) {
        return userDAO.findUserByChatId(chatId);
    }

    void addUserByChatId(User user) {
        userDAO.addUserByChatId(user);
    }
}
