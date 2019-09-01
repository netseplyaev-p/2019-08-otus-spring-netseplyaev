package ru.npv.exam.app.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum QuestionType {
    OPEN_ENDED("Открытый"),
    CLOSE_ENDED("Закрытый"),
    YES_NO("Да/Нет");

    @Getter
    private String defaultName;
}
