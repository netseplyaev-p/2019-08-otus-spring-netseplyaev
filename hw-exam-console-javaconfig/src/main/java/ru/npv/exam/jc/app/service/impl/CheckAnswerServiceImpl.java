package ru.npv.exam.jc.app.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.npv.exam.jc.app.domain.model.AbstractQuestion;
import ru.npv.exam.jc.app.domain.model.QuestionType;
import ru.npv.exam.jc.app.domain.app.CheckAnswerService;
import ru.npv.exam.jc.app.domain.app.QuestionChecker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CheckAnswerServiceImpl implements CheckAnswerService {
    private static final Logger LOG = LoggerFactory.getLogger(CheckAnswerServiceImpl.class);

    private final Map<QuestionType, QuestionChecker> checkers;

    public CheckAnswerServiceImpl(List<QuestionChecker> questionCheckers) {
        LOG.debug("Загрузка checkers:");
        checkers = new HashMap<>();
        for (QuestionChecker ch: questionCheckers) {
            LOG.debug("Загружен checker для {}, класс {}", ch.getQuestionType(), ch.getClass().getName());
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
