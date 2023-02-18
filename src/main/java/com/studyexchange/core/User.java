package com.studyexchange.core;

import java.util.EnumSet;

public class User {
    private final long chatId;
    private String name;
    private UserState userState;
    private int lastBotAnswerDate;
    private final EnumSet<Subject> subjectsCanHelpWith;
    private Grade grade;

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
}
