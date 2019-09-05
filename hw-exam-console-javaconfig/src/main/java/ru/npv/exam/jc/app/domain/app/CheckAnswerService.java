package ru.npv.exam.jc.app.domain.app;

import ru.npv.exam.jc.app.domain.model.AbstractQuestion;

public interface CheckAnswerService {
    boolean check(AbstractQuestion question, String input);
}
