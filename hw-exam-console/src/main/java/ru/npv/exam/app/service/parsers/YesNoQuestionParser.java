package ru.npv.exam.app.service.parsers;

import ru.npv.exam.app.domain.QuestionType;
import ru.npv.exam.app.domain.YesNoQuestion;
import ru.npv.exam.app.service.QuestionParser;

public class YesNoQuestionParser implements QuestionParser<YesNoQuestion, String> {

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
        return null;
    }
}
