package ru.npv.exam.app.service.impl;

import ru.npv.exam.app.domain.AbstractQuestion;
import ru.npv.exam.app.service.CheckAnswerService;

public class CheckAnswerServiceImpl implements CheckAnswerService {
    @Override
    public boolean check(AbstractQuestion question) {
        return false;
    }
}
