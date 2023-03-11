package com.studyexchange.core;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class User {
    private final long chatId;
    private String name;
    private UserState userState;
    private int lastBotAnswerDate;
    private final EnumSet<Subject> subjectsCanHelpWith;
    private Grade grade;
    private boolean active;
    private final Set<Long> seenHelpRequestsIds;

    public static User newUser(long chatId, UserState userState) {
        return new User(
            chatId,
            null,
            userState,
            null
        );
    }

    public User(
        long chatId,
        String name,
        UserState userState,
        Grade grade
    ) {
        this.chatId = chatId;
        this.name = name;
        this.userState = userState;
        this.grade = grade;
        lastBotAnswerDate = 0;
        subjectsCanHelpWith = EnumSet.noneOf(Subject.class);
        seenHelpRequestsIds = new HashSet<>();
        active = false;
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

    public int getLastBotAnswerDate() {
        return lastBotAnswerDate;
    }

    public Grade getGrade() {
        return grade;
    }

    public EnumSet<Subject> getSubjectsCanHelpWith() {
        return subjectsCanHelpWith;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isRequestSeen(HelpRequest helpRequest) {
        return seenHelpRequestsIds.contains(helpRequest.getHelpRequestId());
    }

    public void changeSubjectPresence(Subject subject) {
        if (subjectsCanHelpWith.contains(subject)) {
            subjectsCanHelpWith.remove(subject);
        } else {
            subjectsCanHelpWith.add(subject);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }

    public void setLastBotAnswerDate(int lastBotAnswerDate) {
        this.lastBotAnswerDate = lastBotAnswerDate;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void markRequestSeen(HelpRequest helpRequest) {
        seenHelpRequestsIds.add(helpRequest.getHelpRequestId());
    }
}
