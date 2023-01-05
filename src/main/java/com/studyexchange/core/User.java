package com.studyexchange.core;

public class User {
    private final long chatId;
    private final String name;
    private final String photoFileId;

    public long getChatId() {
        return chatId;
    }

    public String getName() {
        return name;
    }

    public String getPhotoFileId() {
        return photoFileId;
    }

    public User(
            long chatId,
            String name,
            String photoFileId
    ) {
        this.chatId = chatId;
        this.name = name;
        this.photoFileId = photoFileId;
    }


}