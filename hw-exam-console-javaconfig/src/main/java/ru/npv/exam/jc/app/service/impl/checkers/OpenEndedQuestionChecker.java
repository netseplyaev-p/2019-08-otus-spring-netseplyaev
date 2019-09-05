package ru.npv.exam.jc.app.service.impl.checkers;

import org.springframework.stereotype.Component;
import ru.npv.exam.jc.app.domain.model.OpenEndedQuestion;
import ru.npv.exam.jc.app.domain.model.QuestionType;
import ru.npv.exam.jc.app.domain.app.QuestionChecker;

@Component
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
