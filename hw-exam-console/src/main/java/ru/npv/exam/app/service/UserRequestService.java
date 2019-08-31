package ru.npv.exam.app.service;

import ru.npv.exam.app.domain.AbstractQuestion;

public interface UserRequestService {
    AbstractQuestion nextQuestion();
    boolean checkInput(String userInput);
}
