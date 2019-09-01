package ru.npv.exam.app.service.utils;

import org.springframework.util.CollectionUtils;
import ru.npv.exam.app.domain.AbstractQuestion;
import ru.npv.exam.app.domain.QuestionType;

import java.util.List;

public class QuestionUtils {

    public static String asString(AbstractQuestion question) {
        if (question == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder(question.getType().getDefaultName())
                .append(" вопрос: ")
                .append(question.getText());
        List<String> vars = question.getVariants();
        List<String> answs = question.getRightAnsvers();
        if (CollectionUtils.isEmpty(vars)) {
            sb.append("[Нет вариантов]");
        } else {
            if (QuestionType.YES_NO.equals(question.getType())) {
                sb.append(" Варианты[Да, Нет]");
            } else {
                sb.append(" Варианты[");
                for (int i = 0; i < vars.size(); i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(vars.get(i));
                }
                sb.append("]");
            }
        }
        sb.append(" Верный ответ:");
        for (String answ: answs) {
            sb.append(" [").append(answ).append("]");
        }
        return sb.toString();
    }

}
