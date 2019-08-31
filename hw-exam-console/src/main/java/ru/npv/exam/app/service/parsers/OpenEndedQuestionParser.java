package ru.npv.exam.app.service.parsers;

import ru.npv.exam.app.domain.OpenEndingQuestion;
import ru.npv.exam.app.domain.QuestionType;
import ru.npv.exam.app.service.QuestionParser;

public class OpenEndedQuestionParser implements QuestionParser<OpenEndingQuestion, String> {

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
        return null;
    }
}
