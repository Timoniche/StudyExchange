package com.studyexchange.service;

import com.studyexchange.core.User;
import com.studyexchange.core.UserState;
import com.studyexchange.dao.UserDAO;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class UserService {
    private final UserDAO userDAO;

    public UserService(
        UserDAO userDAO
    ) {
        this.userDAO = userDAO;
    }

    public User findUserByChatId(long chatId) {
        return userDAO.findUserByChatId(chatId);
    }

    public void putUser(User user) {
        userDAO.putUser(user);
    }

    public void updateUser(
        @NotNull User user,
        Consumer<User> modification
    ) {
        modification.accept(user);
        putUser(user);
    }

    public void updateUser(long chatId, Consumer<User> modification) {
        updateUser(findUserByChatId(chatId), modification);
    }

    public static void checkUserNotNullOrThrow(User user, UserState userState) {
        if (user == null) {
            throw new IllegalStateException("User with " + userState.toString() + " state must exist");
        }
    }
}
