package ru.npv.exam.app.service.impl.checkers;

import ru.npv.exam.app.domain.CloseEndingQuestion;
import ru.npv.exam.app.domain.QuestionType;
import ru.npv.exam.app.service.QuestionChecker;

public class CloseEndedQuestionChecker implements QuestionChecker<CloseEndingQuestion> {
    @Override
    public QuestionType getQuestionType() {
        return QuestionType.CLOSE_ENDED;
    }

    @Override
    public boolean check(CloseEndingQuestion question, String answer) {
        return false;
    }
}
