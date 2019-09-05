package ru.npv.exam.jc.app.domain.app;

import ru.npv.exam.jc.app.domain.model.AbstractQuestion;
import ru.npv.exam.jc.app.domain.model.QuestionType;

public interface QuestionChecker<T extends AbstractQuestion> {
    QuestionType getQuestionType();
    boolean check(T question, String answer);      // answer: ѕри открытом вопросе - пользовательский ввод, при остальных - текст выбранного варианта.
}
