package com.studyexchange.core;

public enum Subject {
    MATHEMATICS("Математика"),
    PHYSICS("Физика"),
    INFORMATICS("Информатика"),
    RUSSIAN("Русский язык"),
    ENGLISH("Английский язык"),
    CHEMISTRY("Химия"),
    LITERATURE("Литература"),
    GEOGRAPHY("География"),
    SOCIAL("Обществознание"),
    BIOLOGY("Биология"),
    HISTORY("История"),
    OTHER("Другое");

    private final String name;

    Subject(String name) {
        this.name = name;
    }

    public static Subject fromName(String name) {
        for (Subject subject : Subject.values()) {
            if (subject.getName().equalsIgnoreCase(name)) {
                return subject;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String toHashtag() {
        return switch (this) {
            case MATHEMATICS -> "#математика";
            case PHYSICS -> "#физика";
            case INFORMATICS -> "#информатика";
            case RUSSIAN -> "#русский";
            case ENGLISH -> "#английский";
            case CHEMISTRY -> "#химия";
            case LITERATURE -> "#литература";
            case GEOGRAPHY -> "#география";
            case SOCIAL -> "#обществознание";
            case BIOLOGY -> "#биология";
            case HISTORY -> "#история";
            case OTHER -> "#другое";
        };
    }

    public String prepositionalCase() {
        return switch (this) {
            case MATHEMATICS -> "математике";
            case PHYSICS -> "физике";
            case INFORMATICS -> "информатике";
            case RUSSIAN -> "русском языке";
            case ENGLISH -> "английском языке";
            case CHEMISTRY -> "химии";
            case LITERATURE -> "литературе";
            case GEOGRAPHY -> "географии";
            case SOCIAL -> "обществознании";
            case BIOLOGY -> "биологии";
            case HISTORY -> "истории";
            case OTHER -> "";
        };
    }

    public String dativeCase() {
        return switch (this) {
            case MATHEMATICS -> "математике";
            case PHYSICS -> "физике";
            case INFORMATICS -> "информатике";
            case RUSSIAN -> "русскому языку";
            case ENGLISH -> "английскому языку";
            case CHEMISTRY -> "химии";
            case LITERATURE -> "литературе";
            case GEOGRAPHY -> "географии";
            case SOCIAL -> "обществознанию";
            case BIOLOGY -> "биологии";
            case HISTORY -> "истории";
            case OTHER -> "";
        };
    }

}
