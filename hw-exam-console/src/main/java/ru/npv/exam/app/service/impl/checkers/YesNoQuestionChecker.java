package ru.npv.exam.app.service.impl.checkers;

import org.springframework.util.StringUtils;
import ru.npv.exam.app.domain.QuestionType;
import ru.npv.exam.app.domain.YesNoQuestion;
import ru.npv.exam.app.service.QuestionChecker;

public class YesNoQuestionChecker implements QuestionChecker<YesNoQuestion> {
    @Override
    public QuestionType getQuestionType() {
        return QuestionType.YES_NO;
    }

    @Override
    public boolean check(YesNoQuestion question, String answer) {
        if (question.getRightAnsvers().isEmpty()) {
            return false;
        }
        String varText = question.getVariantsMapping().get(answer);
        return !StringUtils.isEmpty(varText) && question.getRightAnsvers().get(0).equals(varText);     // 1 верный ответ
    }
}
