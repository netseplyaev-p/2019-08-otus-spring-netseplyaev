package ru.npv.exam.jc.app.service.impl.parsers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.npv.exam.jc.app.domain.model.CloseEndedQuestion;
import ru.npv.exam.jc.app.domain.model.QuestionType;
import ru.npv.exam.jc.app.domain.app.utils.QuestionUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Component
public class CloseEndedQuestionParser extends AbstractQuestionParser<CloseEndedQuestion, String> {
    private final Logger LOG = LoggerFactory.getLogger(CloseEndedQuestionParser.class);

    public CloseEndedQuestionParser(String separator) {
        super(separator);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.CLOSE_ENDED;
    }

    @Override
    public Class<CloseEndedQuestion> getQuestionClass() {
        return CloseEndedQuestion.class;
    }

    @Override
    public CloseEndedQuestion parse(String input) {
        if (StringUtils.isEmpty(input)) {
            return null;
        }
        String[] parts = input.split(getSeparator());
        List<String> variants = new LinkedList<>();
        for (String variant: Arrays.copyOfRange(parts, 3, parts.length)) {
            variants.add(QuestionUtils.splitCommas(variant));
        }
        CloseEndedQuestion question = new CloseEndedQuestion(QuestionUtils.splitCommas(parts[2]), parts[1].trim(), variants);
        LOG.trace("Попался закрытый вопрос. На выходе - {}", question);
        return question;
    }
}
