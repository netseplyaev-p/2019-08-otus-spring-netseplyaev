package ru.npv.exam.app.service.parsers;

import ru.npv.exam.app.domain.CloseEndingQuestion;
import ru.npv.exam.app.domain.QuestionType;
import ru.npv.exam.app.service.QuestionParser;

public class CloseEndedQuestionParser implements QuestionParser<CloseEndingQuestion, String> {

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
        return null;
    }
}
