package ru.npv.exam.jc.app.domain.app;

public interface UserRequestService<T, V> {
    ExamProcess getExamProcess(T input, V output);
}
