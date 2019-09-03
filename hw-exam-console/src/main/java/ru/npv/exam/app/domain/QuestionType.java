package ru.npv.exam.app.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum QuestionType {
    OPEN_ENDED("��������"),
    CLOSE_ENDED("��������"),
    YES_NO("��/���");

    @Getter
    private String defaultName;
}
