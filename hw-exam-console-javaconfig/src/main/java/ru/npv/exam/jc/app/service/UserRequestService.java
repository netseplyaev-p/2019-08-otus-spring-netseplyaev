package ru.npv.exam.jc.app.service;

public interface UserRequestService<T, V> {
    ExamProcess getExamProcess(T input, V output);
}
