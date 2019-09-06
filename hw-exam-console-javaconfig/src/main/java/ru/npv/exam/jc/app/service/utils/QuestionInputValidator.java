package ru.npv.exam.jc.app.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import ru.npv.exam.jc.app.domain.model.AbstractQuestion;
import ru.npv.exam.jc.app.domain.model.CloseEndedQuestion;
import ru.npv.exam.jc.app.domain.model.OpenEndedQuestion;
import ru.npv.exam.jc.app.domain.model.YesNoQuestion;

public class QuestionInputValidator {
    private static final Logger LOG = LoggerFactory.getLogger(QuestionInputValidator.class);

    public static <T extends AbstractQuestion> boolean validate(T question, String input) {
        if (question.getClass().equals(YesNoQuestion.class))
            return validate((YesNoQuestion) question, input);
        if (question.getClass().equals(CloseEndedQuestion.class))
            return validate((CloseEndedQuestion) question, input);
        if (question.getClass().equals(OpenEndedQuestion.class))
            return validate((OpenEndedQuestion) question, input);
        LOG.debug("Undefined question type: {}", question.getClass());
        return true;
    }

    public static boolean validate(YesNoQuestion q, String input) {
        try {
            int val = Integer.valueOf(input);
            return val==1 || val==2;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validate(CloseEndedQuestion q, String input) {
        try {
            int val = Integer.valueOf(input);
            return val > 0 && val < q.getVariants().size()+1;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validate(OpenEndedQuestion q, String input) {
        return !StringUtils.isEmpty(input);
    }
}
