package ru.npv.exam.app.service;

import ru.npv.exam.app.domain.AbstractQuestion;

public interface UserRequestService<T, V> {
    void init(T input, V output);
    AbstractQuestion nextQuestion();    // null - если не осталось незаданных вопросов
    boolean checkInput(String userInput);
}
