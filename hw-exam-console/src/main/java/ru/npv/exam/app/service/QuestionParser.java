package ru.npv.exam.app.service;

import ru.npv.exam.app.domain.AbstractQuestion;
import ru.npv.exam.app.domain.QuestionType;

public interface QuestionParser<T extends AbstractQuestion, K extends Object> {
    default Class<K> getInputClass() {
        return (Class<K>) String.class;
    }
    QuestionType getQuestionType();
    Class<T> getQuestionClass();
    T parse(K input);
}