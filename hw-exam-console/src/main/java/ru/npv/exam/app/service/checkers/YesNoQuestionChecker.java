package ru.npv.exam.app.service.checkers;

import ru.npv.exam.app.domain.YesNoQuestion;
import ru.npv.exam.app.service.QuestionChecker;

public class YesNoQuestionChecker implements QuestionChecker<YesNoQuestion> {
    @Override
    public boolean check(YesNoQuestion question, String answer) {
        return false;
    }
}
