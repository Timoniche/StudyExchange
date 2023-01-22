package com.studyexchange.core;

public class User {
    private final long chatId;
    private String name;
    private UserState userState;

    public static User newUser(long chatId, UserState userState) {
        return new User(
            chatId,
            null,
            userState
        );
    }

    public User(
        long chatId,
        String name,
        UserState userState
    ) {
        this.chatId = chatId;
        this.name = name;
        this.userState = userState;
    }

    public long getChatId() {
        return chatId;
    }

    public String getName() {
        return name;
    }

    public UserState getUserState() {
        return userState;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }
}
