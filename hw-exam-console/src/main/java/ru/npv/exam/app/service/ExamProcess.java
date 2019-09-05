package ru.npv.exam.app.service;

import java.util.Map;

public interface ExamProcess {
    void setParameters(Map<String, ?> parameters);
    void run();
}
