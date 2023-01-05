package com.studyexchange.dao;

import com.studyexchange.core.User;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserDAO implements UserDAO {

    private final Map<Long, User> userByChatId;

    public InMemoryUserDAO() {
        this.userByChatId = new HashMap<>();
    }

    @Override
    public User findUserByChatId(long chatId) {
        return userByChatId.get(chatId);
    }

    @Override
    public void addUserByChatId(User user) {
        userByChatId.put(user.getChatId(), user);
    }

}
