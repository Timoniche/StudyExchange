package com.studyexchange.core;

public class User {
    private final long chatId;
    private String name;
    private String photoFileId;
    private UserState userState;

    public static User newUser(long chatId, UserState userState) {
        return new User(
                chatId,
                null,
                null,
                userState
        );
    }

    public User(
            long chatId,
            String name,
            String photoFileId,
            UserState userState
    ) {
        this.chatId = chatId;
        this.name = name;
        this.photoFileId = photoFileId;
        this.userState = userState;
    }

    public long getChatId() {
        return chatId;
    }

    public String getName() {
        return name;
    }

    public String getPhotoFileId() {
        return photoFileId;
    }

    public UserState getUserState() {
        return userState;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhotoFileId(String photoFileId) {
        this.photoFileId = photoFileId;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }
}
