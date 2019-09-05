package ru.npv.exam.app.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.npv.exam.app.domain.AbstractQuestion;
import ru.npv.exam.app.domain.QuestionType;
import ru.npv.exam.app.service.CheckAnswerService;
import ru.npv.exam.app.service.QuestionChecker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckAnswerServiceImpl implements CheckAnswerService {
    private final Logger LOG = LoggerFactory.getLogger(CheckAnswerServiceImpl.class);

    private final Map<QuestionType, QuestionChecker> checkers;

    public CheckAnswerServiceImpl(List<QuestionChecker> questionCheckers) {
        LOG.debug("Загрузка checkers:");
        checkers = new HashMap<>();
        for (QuestionChecker ch: questionCheckers) {
            LOG.debug("Для {} - {}", ch.getQuestionType(), ch.getClass().getName());
            checkers.put(ch.getQuestionType(), ch);
        }
    }

    @Override
    public boolean check(AbstractQuestion question, String input) {
        if (checkers.containsKey(question.getType())) {
            return checkers.get(question.getType()).check(question, input);
        } else {
            LOG.debug("Невозможно поверить вопрос {}, отсутствует checker", question.getText());
            return false;
        }
    }
}
