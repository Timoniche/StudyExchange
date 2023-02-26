package com.studyexchange.core;

public enum Grade {
    FIRST("1 класс", 1),
    SECOND("2 класс", 2),
    THIRD("3 класс", 3),
    FOURTH("4 класс", 4),
    FIFTH("5 класс", 5),
    SIXTH("6 класс", 6),
    SEVENTH("7 класс", 7),
    EIGHTH("8 класс", 8),
    NINTH("9 класс", 9),
    TENTH("10 класс", 10),
    ELEVENTH("11 класс", 11),
    GRADUATED("Закончил(а) школу", 12);

    private final String gradeName;
    private final int gradeNumber;

    Grade(String gradeName, int gradeNumber) {
        this.gradeName = gradeName;
        this.gradeNumber = gradeNumber;
    }

    public String toHashtag() {
        return switch (this) {
            case FIRST -> "#1_класс";
            case SECOND -> "#2_класс";
            case THIRD -> "#3_класс";
            case FOURTH -> "#4_класс";
            case FIFTH -> "#5_класс";
            case SIXTH -> "#6_класс";
            case SEVENTH -> "#7_класс";
            case EIGHTH -> "#8_класс";
            case NINTH -> "#9_класс";
            case TENTH -> "#10_класс";
            case ELEVENTH -> "#11_класс";
            case GRADUATED -> "#выпускник";
        };
    }

    public static Grade fromName(String name) {
        for (Grade grade : Grade.values()) {
            if (grade.getGradeName().equalsIgnoreCase(name)) {
                return grade;
            }
        }
        return null;
    }

    public String getGradeName() {
        return gradeName;
    }

    public int getGradeNumber() {
        return gradeNumber;
    }
}
