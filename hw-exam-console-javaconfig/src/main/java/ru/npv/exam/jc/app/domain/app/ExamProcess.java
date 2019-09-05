package ru.npv.exam.jc.app.domain.app;

import java.util.Map;

public interface ExamProcess extends Runnable{
    void setParameters(Map<String, ?> parameters);
}
