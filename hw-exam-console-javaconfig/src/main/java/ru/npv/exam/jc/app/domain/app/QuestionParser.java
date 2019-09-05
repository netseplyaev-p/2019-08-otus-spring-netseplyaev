package ru.npv.exam.jc.app.domain.app;

import ru.npv.exam.jc.app.domain.model.AbstractQuestion;
import ru.npv.exam.jc.app.domain.model.QuestionType;

public interface QuestionParser<T extends AbstractQuestion, K> {
    QuestionType getQuestionType();
    Class<T> getQuestionClass();
    T parse(K input);
}