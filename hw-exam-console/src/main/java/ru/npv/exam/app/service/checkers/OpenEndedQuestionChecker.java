package ru.npv.exam.app.service.checkers;

import ru.npv.exam.app.domain.OpenEndingQuestion;
import ru.npv.exam.app.domain.QuestionType;
import ru.npv.exam.app.service.QuestionChecker;

public class OpenEndedQuestionChecker implements QuestionChecker<OpenEndingQuestion> {
    @Override
    public QuestionType getQuestionType() {
        return QuestionType.OPEN_ENDED;
    }

    @Override
    public boolean check(OpenEndingQuestion question, String answer) {
        return false;
    }
}
