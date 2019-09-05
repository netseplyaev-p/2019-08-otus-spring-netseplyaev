package ru.npv.exam.jc.app.service;

import ru.npv.exam.jc.app.domain.AbstractQuestion;
import ru.npv.exam.jc.app.domain.QuestionType;

public interface QuestionParser<T extends AbstractQuestion, K> {
    QuestionType getQuestionType();
    Class<T> getQuestionClass();
    T parse(K input);
}