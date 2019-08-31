package ru.npv.exam.app.service.impl.parsers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.npv.exam.app.domain.CloseEndingQuestion;
import ru.npv.exam.app.domain.QuestionType;
import ru.npv.exam.app.service.QuestionParser;

public class CloseEndedQuestionParser implements QuestionParser<CloseEndingQuestion, String> {
    private final Logger LOG = LoggerFactory.getLogger(CloseEndedQuestionParser.class);

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.CLOSE_ENDED;
    }

    @Override
    public Class<CloseEndingQuestion> getQuestionClass() {
        return CloseEndingQuestion.class;
    }

    @Override
    public CloseEndingQuestion parse(String input) {
        LOG.debug("Попался закрытый вопрос");
        return null;
    }
}
