package com.studyexchange.core;

import java.util.List;

public class HelpRequest {
    private final long chatId;
    private final Subject subject;

    private long helpRequestId;
    private String description;
    private List<String> photoFileIds;

    public HelpRequest(
        long chatId,
        Subject subject
    ) {
        this.chatId = chatId;
        this.subject = subject;
    }

    public long getChatId() {
        return chatId;
    }

    public Subject getSubject() {
        return subject;
    }

    public long getHelpRequestId() {
        return helpRequestId;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getPhotoFileIds() {
        return photoFileIds;
    }

    public void setHelpRequestId(long helpRequestId) {
        this.helpRequestId = helpRequestId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhotoFileIds(List<String> photoFileIds) {
        this.photoFileIds = photoFileIds;
    }
}
