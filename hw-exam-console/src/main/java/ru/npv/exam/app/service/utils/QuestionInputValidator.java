package ru.npv.exam.app.service.utils;

import ru.npv.exam.app.domain.AbstractQuestion;
import ru.npv.exam.app.domain.CloseEndedQuestion;
import ru.npv.exam.app.domain.OpenEndedQuestion;
import ru.npv.exam.app.domain.YesNoQuestion;

public class QuestionInputValidator {

    public static <T extends AbstractQuestion> boolean validate(T question, String input) {
        if (question.getClass().equals(YesNoQuestion.class))
            return validate((YesNoQuestion) question, input);
        if (question.getClass().equals(CloseEndedQuestion.class))
            return validate((CloseEndedQuestion) question, input);
        if (question.getClass().equals(OpenEndedQuestion.class))
            return validate((OpenEndedQuestion) question, input);
        throw new UnsupportedOperationException("Неопознанный тип вопроса");
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
        return true;
    }
}
