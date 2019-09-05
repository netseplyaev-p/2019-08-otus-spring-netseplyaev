package ru.npv.exam.jc.app.service.impl.checkers;

import ru.npv.exam.jc.app.domain.OpenEndedQuestion;
import ru.npv.exam.jc.app.domain.QuestionType;
import ru.npv.exam.jc.app.service.QuestionChecker;

public class OpenEndedQuestionChecker implements QuestionChecker<OpenEndedQuestion> {
    @Override
    public QuestionType getQuestionType() {
        return QuestionType.OPEN_ENDED;
    }

    @Override
    public boolean check(OpenEndedQuestion question, String answer) {
        for (String rightAnsw: question.getRightAnswers()) {
            if (rightAnsw.trim().equalsIgnoreCase(answer.trim())) {
                return true;
            }
        }
        return false;
    }
}
