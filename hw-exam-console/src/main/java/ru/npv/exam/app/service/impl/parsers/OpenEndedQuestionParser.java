package ru.npv.exam.app.service.impl.parsers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.npv.exam.app.domain.OpenEndingQuestion;
import ru.npv.exam.app.domain.QuestionType;
import ru.npv.exam.app.service.QuestionParser;

public class OpenEndedQuestionParser implements QuestionParser<OpenEndingQuestion, String> {
    private final Logger LOG = LoggerFactory.getLogger(OpenEndedQuestionParser.class);

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.OPEN_ENDED;
    }

    @Override
    public Class<OpenEndingQuestion> getQuestionClass() {
        return OpenEndingQuestion.class;
    }

    @Override
    public OpenEndingQuestion parse(String input) {
        LOG.debug("Попался открытый вопрос");
        return null;
    }
}
