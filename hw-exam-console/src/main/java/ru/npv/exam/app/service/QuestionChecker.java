package ru.npv.exam.app.service;

import ru.npv.exam.app.domain.AbstractQuestion;
import ru.npv.exam.app.domain.QuestionType;

public interface QuestionChecker<T extends AbstractQuestion> {
    QuestionType getQuestionType();
    boolean check(T question, String answer);
}
