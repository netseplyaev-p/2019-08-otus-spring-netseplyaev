package ru.npv.exam.app.service;

public interface UserRequestService<T, V> {
    ExamProcess getExamProcess(T input, V output);
}
