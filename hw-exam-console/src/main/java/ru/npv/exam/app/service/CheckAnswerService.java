package ru.npv.exam.app.service;

import ru.npv.exam.app.domain.AbstractQuestion;

public interface CheckAnswerService {
    boolean check(AbstractQuestion question, String input);
}
