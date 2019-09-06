package ru.npv.exam.jc.app.service.utils;

import org.springframework.util.CollectionUtils;
import ru.npv.exam.jc.app.domain.model.AbstractQuestion;
import ru.npv.exam.jc.app.domain.model.QuestionType;

import java.util.List;

public class QuestionUtils {

    public static String splitCommas(String in) {
        String out = in.trim();
        if (out.startsWith("\"") || out.startsWith("\'")) {
            out = out.substring(1);
        }
        if (out.endsWith("\"") || out.endsWith("\'")) {
            out = out.substring(0, out.length()-1);
        }
        return out;
    }

    public static String asString(AbstractQuestion question) {
        if (question == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder(question.getType().getDefaultName())
                .append(" ������: [")
                .append(question.getText())
                .append("]");
        List<String> vars = question.getVariants();
        List<String> answs = question.getRightAnswers();
        if (CollectionUtils.isEmpty(vars)) {
            sb.append("[��� ���������]");
        } else {
            if (QuestionType.YES_NO.equals(question.getType())) {
                sb.append(" ��������[��, ���]");
            } else {
                sb.append(" ��������[");
                for (int i = 0; i < vars.size(); i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(vars.get(i));
                }
                sb.append("]");
            }
        }
        sb.append(" ������ �����:");
        for (String answ: answs) {
            sb.append(" [").append(answ).append("]");
        }
        return sb.toString();
    }
}
