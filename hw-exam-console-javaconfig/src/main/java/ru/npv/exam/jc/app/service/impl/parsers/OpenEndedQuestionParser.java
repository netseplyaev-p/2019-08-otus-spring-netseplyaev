package ru.npv.exam.jc.app.service.impl.parsers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.npv.exam.jc.app.domain.model.OpenEndedQuestion;
import ru.npv.exam.jc.app.domain.model.QuestionType;
import ru.npv.exam.jc.app.domain.app.utils.QuestionUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Component
public class OpenEndedQuestionParser extends AbstractQuestionParser<OpenEndedQuestion, String> {
    private static final Logger LOG = LoggerFactory.getLogger(OpenEndedQuestionParser.class);

    public OpenEndedQuestionParser(@Value("${parts.separator}") String separator) {
        super(separator);
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.OPEN_ENDED;
    }

    @Override
    public Class<OpenEndedQuestion> getQuestionClass() {
        return OpenEndedQuestion.class;
    }

    @Override
    public OpenEndedQuestion parse(String input) {

        if (StringUtils.isEmpty(input)) {
            return null;
        }
        String[] parts = input.split(getSeparator());
        List<String> rightVariants = new LinkedList<>();
        for (String rightVariant: Arrays.copyOfRange(parts, 2, parts.length)) {
            rightVariants.add(QuestionUtils.splitCommas(rightVariant));
        }
        OpenEndedQuestion question = new OpenEndedQuestion(QuestionUtils.splitCommas(parts[1]), rightVariants);
        LOG.trace("Попался открытый вопрос. На выходе - {}", question);
        return question;
    }
}