package com.studyexchange.dao.inmemory;

import com.studyexchange.core.User;
import com.studyexchange.dao.UserDAO;

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
    public void putUser(User user) {
        userByChatId.put(user.getChatId(), user);
    }

}
