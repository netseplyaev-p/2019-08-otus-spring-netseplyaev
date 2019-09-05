package ru.npv.exam.app.service.impl.parsers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.npv.exam.app.domain.AbstractQuestion;
import ru.npv.exam.app.service.QuestionParser;

@AllArgsConstructor
public abstract class AbstractQuestionParser<T extends AbstractQuestion, V> implements QuestionParser<T, V> {

    @Getter
    private final String separator;
}
