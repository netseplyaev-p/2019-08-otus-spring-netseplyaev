package ru.npv.exam.app.service;

import ru.npv.exam.app.domain.AbstractQuestion;

public interface QuestionChecker<T extends AbstractQuestion> {
    boolean check(T question, String answer);
}
