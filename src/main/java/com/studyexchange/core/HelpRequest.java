package com.studyexchange.core;

import java.util.List;

import static com.studyexchange.telegramapiutils.MessagingUtils.NEXT_LINE;
import static com.studyexchange.telegramapiutils.MessagingUtils.bold;
import static com.studyexchange.telegramapiutils.MessagingUtils.escapeMarkdownV2;

public class HelpRequest {
    private final long chatId;
    private final Subject subject;

    private long helpRequestId;
    private String description;
    private List<String> photoFileIds;
    private boolean active;

    public HelpRequest(
        long chatId,
        Subject subject
    ) {
        this.chatId = chatId;
        this.subject = subject;
        active = false;
    }

    public String helpRequestFormText(
        String userName,
        Grade grade
    ) {
        return ""
            + escapeMarkdownV2(subject.toHashtag()) + " "
            + escapeMarkdownV2(grade.toHashtag())
            + NEXT_LINE
            + NEXT_LINE
            + bold("Кому нужна помощь: ") + escapeMarkdownV2(userName)
            + NEXT_LINE
            + NEXT_LINE
            + bold("Описание задачи: ")
            + NEXT_LINE
            + escapeMarkdownV2(description);
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

    public boolean isActive() {
        return active;
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

    public void setActive(boolean active) {
        this.active = active;
    }
}
