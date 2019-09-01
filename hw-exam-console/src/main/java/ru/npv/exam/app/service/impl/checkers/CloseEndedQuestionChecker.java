package ru.npv.exam.app.service.impl.checkers;

import ru.npv.exam.app.domain.CloseEndedQuestion;
import ru.npv.exam.app.domain.QuestionType;
import ru.npv.exam.app.service.QuestionChecker;

public class CloseEndedQuestionChecker implements QuestionChecker<CloseEndedQuestion> {
    @Override
    public QuestionType getQuestionType() {
        return QuestionType.CLOSE_ENDED;
    }

    @Override
    public boolean check(CloseEndedQuestion question, String answer) {
        return false;
    }
}
