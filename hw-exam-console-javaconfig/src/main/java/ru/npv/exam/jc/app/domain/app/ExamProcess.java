package ru.npv.exam.jc.app.domain.app;

import java.util.Map;

public interface ExamProcess {
    void setParameters(Map<String, ?> parameters);
    void run();
}
