package ru.npv.exam.app.service;

import ru.npv.exam.app.domain.AbstractQuestion;

import java.util.List;

public interface LoadQuestionsService {

    String getDefaultPath();
    List<AbstractQuestion> load(String resourcePath);
    default List<AbstractQuestion> load() {
        return load(getDefaultPath());
    }
}
