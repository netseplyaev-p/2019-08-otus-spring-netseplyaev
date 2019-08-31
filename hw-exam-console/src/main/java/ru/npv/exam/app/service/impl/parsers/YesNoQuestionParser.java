package ru.npv.exam.app.service.impl.parsers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.npv.exam.app.domain.QuestionType;
import ru.npv.exam.app.domain.YesNoQuestion;
import ru.npv.exam.app.service.QuestionParser;

public class YesNoQuestionParser implements QuestionParser<YesNoQuestion, String> {
    private final Logger LOG = LoggerFactory.getLogger(YesNoQuestionParser.class);

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
        LOG.debug("Попался вопрос Да/Нет");
        return null;
    }
}
