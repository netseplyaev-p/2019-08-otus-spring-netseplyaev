package ru.npv.exam.app.service.impl.parsers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import ru.npv.exam.app.domain.QuestionType;
import ru.npv.exam.app.domain.YesNoQuestion;
import ru.npv.exam.app.service.utils.QuestionUtils;

import java.util.HashMap;
import java.util.Map;

public class YesNoQuestionParser extends AbstractQuestionParser<YesNoQuestion, String> {
    private final Logger LOG = LoggerFactory.getLogger(YesNoQuestionParser.class);
    private final Map<String, String> defaultMapping;

    public YesNoQuestionParser(String separator) {
        super(separator);
        defaultMapping = new HashMap<>();
        defaultMapping.put("Да", "Y");
        defaultMapping.put("Нет", "N");
    }

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.YES_NO;
    }

    @Override
    public Class<YesNoQuestion> getQuestionClass() {
        return YesNoQuestion.class;
    }

    @Override
    public YesNoQuestion parse(String input) {
        if (StringUtils.isEmpty(input)) {
            return null;
        }
        String[] parts = input.split(getSeparator());
        YesNoQuestion question = new YesNoQuestion(QuestionUtils.splitCommas(parts[2]), defaultMapping, parts[1].trim());
        LOG.debug("Попался вопрос Да/Нет. На выходе - {}", question);
        return question;
    }
}
