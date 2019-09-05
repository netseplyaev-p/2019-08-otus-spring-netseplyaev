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
        if (question.getRightAnswers().isEmpty() || StringUtils.isEmpty(answer)) {
            return false;
        }
        String varText = question.getVariants().get(Integer.valueOf(answer)-1);
        return question.getRightAnswers().get(0).equals(question.getVariantsMapping().get(varText));     // 1 ������ �����
    }
}
