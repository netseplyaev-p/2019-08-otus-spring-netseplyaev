package ru.npv.exam.app.service.impl.checkers;

import ru.npv.exam.app.domain.CloseEndedQuestion;
import ru.npv.exam.app.domain.QuestionType;
import ru.npv.exam.app.service.QuestionChecker;

import java.util.Objects;

public class CloseEndedQuestionChecker implements QuestionChecker<CloseEndedQuestion> {

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.CLOSE_ENDED;
    }

    @Override
    public boolean check(CloseEndedQuestion question, String answer) {
        if (question.getRightAnswers().isEmpty()) {
            return false;
        }
        int number = Integer.valueOf(question.getRightAnswers().get(0));        // 1 верный ответ
        int answ = Integer.valueOf(answer);
        return Objects.equals(number, answ);
    }
}
