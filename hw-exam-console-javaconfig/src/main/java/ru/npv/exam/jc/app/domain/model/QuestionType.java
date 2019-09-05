package ru.npv.exam.jc.app.domain.model;

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
