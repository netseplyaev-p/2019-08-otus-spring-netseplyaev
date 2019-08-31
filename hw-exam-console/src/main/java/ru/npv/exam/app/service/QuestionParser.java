package ru.npv.exam.app.service;

import ru.npv.exam.app.domain.AbstractQuestion;
import ru.npv.exam.app.domain.QuestionType;

public interface QuestionParser<T extends AbstractQuestion, K extends Object> {
    QuestionType getQuestionType();
    Class<T> getQuestionClass();
    T parse(K input);
}