package ru.npv.exam.app.service.utils;

import ru.npv.exam.app.domain.AbstractQuestion;
import ru.npv.exam.app.domain.CloseEndedQuestion;
import ru.npv.exam.app.domain.OpenEndedQuestion;
import ru.npv.exam.app.domain.YesNoQuestion;

public class QuestionInputValidator {

    public static <T extends AbstractQuestion> boolean validate(T question, String input) {
        return validate(question, input);
    }

    public static boolean validate(YesNoQuestion q, String input) {
        try {
            Integer.valueOf(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validate(CloseEndedQuestion q, String input) {
        try {
            Integer.valueOf(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validate(OpenEndedQuestion q, String input) {
        return true;
    }
}
