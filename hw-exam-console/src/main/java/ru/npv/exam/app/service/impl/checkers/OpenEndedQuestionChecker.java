package ru.npv.exam.app.service.impl.checkers;

import ru.npv.exam.app.domain.OpenEndedQuestion;
import ru.npv.exam.app.domain.QuestionType;
import ru.npv.exam.app.service.QuestionChecker;

public class OpenEndedQuestionChecker implements QuestionChecker<OpenEndedQuestion> {
    @Override
    public QuestionType getQuestionType() {
        return QuestionType.OPEN_ENDED;
    }

    @Override
    public boolean check(OpenEndedQuestion question, String answer) {
        for (String rightAnsw: question.getRightAnsvers()) {
            if (rightAnsw.equalsIgnoreCase(answer)) {
                return true;
            }
        }
        return false;
    }
}
