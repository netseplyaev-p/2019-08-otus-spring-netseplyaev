package ru.npv.exam.app.service.impl.checkers;

import lombok.Getter;
import ru.npv.exam.app.domain.CloseEndedQuestion;
import ru.npv.exam.app.domain.QuestionType;
import ru.npv.exam.app.service.QuestionChecker;

public class CloseEndedQuestionChecker implements QuestionChecker<CloseEndedQuestion> {
    @Getter
    private final String delimiter;

    public CloseEndedQuestionChecker(String delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.CLOSE_ENDED;
    }

    @Override
    public boolean check(CloseEndedQuestion question, String answer) {
        if (question.getRightAnsvers().isEmpty()) {
            return false;
        }
        int number = Integer.valueOf(question.getRightAnsvers().get(0));        // 1 верный ответ
        return question.getVariants().get(number).equals(answer);
    }
}
