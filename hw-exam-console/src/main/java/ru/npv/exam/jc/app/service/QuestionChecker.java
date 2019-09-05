package ru.npv.exam.jc.app.service;

import ru.npv.exam.jc.app.domain.AbstractQuestion;
import ru.npv.exam.jc.app.domain.QuestionType;

public interface QuestionChecker<T extends AbstractQuestion> {
    QuestionType getQuestionType();
    boolean check(T question, String answer);      // answer: ѕри открытом вопросе - пользовательский ввод, при остальных - текст выбранного варианта.
}
