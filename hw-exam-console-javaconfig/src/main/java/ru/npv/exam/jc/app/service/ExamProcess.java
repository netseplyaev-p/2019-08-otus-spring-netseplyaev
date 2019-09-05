package ru.npv.exam.jc.app.service;

import java.util.Map;

public interface ExamProcess {
    void setParameters(Map<String, ?> parameters);
    void run();
}
