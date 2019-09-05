package ru.npv.exam.jc.app.service;

import ru.npv.exam.jc.app.domain.AbstractQuestion;

public interface CheckAnswerService {
    boolean check(AbstractQuestion question, String input);
}
